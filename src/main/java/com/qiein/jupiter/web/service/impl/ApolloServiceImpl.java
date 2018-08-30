package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.retrofitUtil.RetorfitUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.remote.ApolloRemoteService;
import com.qiein.jupiter.web.service.ApolloService;
import com.qiein.jupiter.web.service.LoginService;
import com.qiein.jupiter.web.service.StaffService;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import java.util.Date;


/**
 * apollo
 *
 * @Author: shiTao
 */
@Service
public class ApolloServiceImpl implements ApolloService, ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${apollo.baseUrl}")
    private String apolloBaseUrl;


    private String apolloIps = "";

    @Autowired
    private StaffService staffService;

    @Autowired
    private LoginService loginService;

    private ApolloRemoteService apolloRemoteService;


    /**
     * 获取用户token
     *
     * @param companyId
     * @param staffId
     * @param ip
     * @return
     */
    @Override
    public VerifyParamDTO getUserVerifyInfoByUidCid(int companyId, int staffId, String ip) {
        if (!checkIp(ip)) {
            log.error("非法IP请求:" + ip);
            return null;
        }
        VerifyParamDTO verifyParamDTO = new VerifyParamDTO();
        StaffPO staff = staffService.getById(staffId, companyId);
        if (staff != null) {
            verifyParamDTO.setCid(companyId);
            verifyParamDTO.setUid(staffId);
            //token 为空则重新生成
            String token = staff.getToken();
            if (StringUtil.isEmpty(token)) {
                token = loginService.updateToken(staff);
            }
            verifyParamDTO.setToken(token);
        }
        return verifyParamDTO;
    }

    /**
     * 获取apollo ip
     */
    @Override
    public void getApolloIp() {
        String ip = RetorfitUtil.request(apolloRemoteService.getApolloIp());
        ip = ip.replaceAll("\"", "");
        this.apolloIps = ip;
    }

    /**
     * 获取CRM 地址
     *
     * @return
     */
    @Override
    public String getCrmUrlByCidFromApollo(int cid) {
        String body = RetorfitUtil.request(apolloRemoteService.getCrmUrlByCidFromApollo(cid));
        String url = JSON.parseObject(body).getString("data");
        if (StringUtil.isEmpty(url)) {
            throw new RException(ExceptionEnum.APOLLO_URL_NOT_SET);
        }
        url = HttpUtil.formatEndUrl(url);
        return url;
    }

    /**
     * 发送Websocket 消息
     *
     * @param msg
     */
    @Override
    public void postWebSocketMsg(String msg) {
        RequestBody requestBody = RetorfitUtil.createJSONBody(msg);
        RetorfitUtil.request(apolloRemoteService.postWebSocketMsg(requestBody));
    }

    /**
     * 调用 远程图片
     */
    @Override
    public JSONArray getSrcImgListRpc(String type, int companyId) {
        JSONObject params = new JSONObject();
        params.put("companyId", companyId);
        params.put("typeCode", type);
        RequestBody requestBody = RetorfitUtil.createJSONBody(params.toString());
        String request = RetorfitUtil.request(apolloRemoteService.getSrcImgListRpc(MD5Util.getApolloMd5(params.toString()),
                new Date().getTime(), requestBody));
        JSONObject json = JSON.parseObject(request);
        if (json.getIntValue("code") == CommonConstant.DEFAULT_SUCCESS_CODE) {
            return json.getJSONArray("data");
        }
        return new JSONArray();
    }

    /**
     * 校验是否在 安全IP 内
     *
     * @param ip
     * @return
     */
    private boolean checkIp(String ip) {
        if (StringUtil.isNotEmpty(apolloIps)) {
            String[] ipsArr = apolloIps.split(CommonConstant.STR_SEPARATOR);
            for (String ipTrue : ipsArr) {
                if (ip.equals(ipTrue)) {
                    return true;
                }
            }

        }
        return false;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) {
        Retrofit retrofit = new Retrofit.Builder()
                //设置网络请求的Url地址
                .baseUrl(apolloBaseUrl)
                .build();
        // 创建网络请求接口的实例
        apolloRemoteService = retrofit.create(ApolloRemoteService.class);
    }
}
