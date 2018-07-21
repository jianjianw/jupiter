package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ApolloService;
import com.qiein.jupiter.web.service.LoginService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * apollo
 *
 * @Author: shiTao
 */
@Service
public class ApolloServiceImpl implements ApolloService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${apollo.getIpUrl}")
    private String getIpUrl = "";

    @Value("${apollo.getCrmUrl}")
    private String getCrmUrl = "";

    private String apolloIps = "";

    @Autowired
    private StaffService staffService;

    @Autowired
    private LoginService loginService;


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
        String ip = HttpClient.get(getIpUrl).asString();
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
        JSONObject json = HttpClient.get(getCrmUrl)
                .queryString("cid", cid)
                .asBean(JSONObject.class);
        String url = json.getString("data");
        if (StringUtil.isEmpty(url)) {
            throw new RException(ExceptionEnum.APOLLO_URL_NOT_SET);
        }
        return HttpUtil.formatEndUrl(url);
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
}
