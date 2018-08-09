package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.AppolloUrlConst;
import com.qiein.jupiter.constant.CallUrlConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.util.alicloud.BatchTokenRetrieval;
import com.qiein.jupiter.util.alicloud.RandomString;
import com.qiein.jupiter.web.entity.po.CallPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.AliOauthVO;
import com.qiein.jupiter.web.service.CallService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
@Service
public class CallServiceImpl implements CallService {
    @Value("${apollo.baseUrl}")
    private String appoloBaseUrl;
    @Value("${alicloud.username}")
    private String username;
    @Value("${alicloud.password}")
    private String password;
    @Value("${alicloud.clientId}")
    private String clientId;
    @Value("${alicloud.clientSecret}")
    private String clientSecret;
    @Value("${alicloud.registeredCallbackUrl}")
    private String registeredCallBackUrl;

    @Override
    public void startBack2BackCall(String caller, String callee, StaffPO staffPO) {
        if(StringUtil.isEmpty(caller) || StringUtil.isEmpty(callee)){
            throw new RException(ExceptionEnum.CALLER_OR_CALLEE_IS_NULL);
        }
        //Appollo接口获取intsanceId
        String sign= MD5Util.getApolloMd5(String.valueOf(staffPO.getCompanyId()));
        String json= HttpClient
                // 请求方式和请求url
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_CALL_INSTANCE))
                // post提交json
                .queryString("companyId",staffPO.getCompanyId())
                .queryString("sign",sign)
                .asString();
        CallPO callPO = JSONObject.parseObject(JSONObject.parseObject(json).get("data").toString(),CallPO.class);
        //获取Token
        String token = BatchTokenRetrieval.retrieve(username, password, clientId, clientSecret, registeredCallBackUrl);
        AliOauthVO aliOauthVO = JSONObject.parseObject(token, AliOauthVO.class);
        //生成时间戳
        StringBuilder timeStamp = new StringBuilder( TimeUtil.format(TimeUtil.localToUTC(TimeUtil.getSysTime()), "yyyy-MM-dd HH:mm:ss")).replace(10,11,"T").append("Z");
        RandomString randomString = new RandomString(45);
         HttpClient
                .get(CallUrlConst.CALL_BASE_URL)
                .queryString("Action", "StartBack2BackCall")
                .queryString("InstanceId", callPO.getInstanceId())
                .queryString("CallCenterNumber", callPO.getPhoneNumber())
                .queryString("Caller", caller)
                .queryString("Callee", callee)
                //公共参数
                .queryString("Format","JSON")
                .queryString("Version","2017-07-05")
                .queryString("Timestamp",timeStamp.toString())
                .queryString("SignatureType","BEARERTOKEN")
                .queryString("RegionId","cn-shanghai")
                .queryString("SignatureNonce", randomString.nextString())
                .queryString("BearerToken",aliOauthVO.getAccessToken()).asString();


    }

}
