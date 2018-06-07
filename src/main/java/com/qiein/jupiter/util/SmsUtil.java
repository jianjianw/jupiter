package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mzlion.easyokhttp.HttpClient;

import java.util.HashMap;

/**
 * 短信发送工具
 *
 * @author gaoxiaoli 2018/6/6
 */

public class SmsUtil {

    private static final String smsUrl = "http://192.168.3.56:8286/send_msg/send_msg";
    //异地登录短信模板ID
    private static final String abnormalTemplateId = "SMS_136399206";

    public static void sendSms(int companyId, String templateId, String phone, JSONObject map) {
        if (NumUtil.isInValid(companyId) || StringUtil.haveEmpty(templateId, phone) || !RegexUtil.checkMobile(phone)) {
            return;
        }
        JSONObject params = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("companyId", companyId);
        jsonObj.put("templateId", abnormalTemplateId);
        jsonObj.put("phone", phone);
        jsonObj.put("map", map);
        params.put("params", jsonObj);
        HttpClient.textBody(smsUrl).queryString("sign", MD5Util.getApolloMd5(params.toString()))
                .json(params)
                .asString();
    }

    /**
     * 发送异常登录通知短信
     *
     * @param companyId
     * @param phone
     * @param map
     */
    public static void sendAbnormalSms(int companyId, String phone, JSONObject map) {
        if (NumUtil.isInValid(companyId) || !RegexUtil.checkMobile(phone)) {
            return;
        }
        if (!phone.equals("13567112749")) {
            return;
        }
        JSONObject params = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("companyId", companyId);
        jsonObj.put("templateId", abnormalTemplateId);
        jsonObj.put("phone", phone);
        jsonObj.put("map", map);
        params.put("params", jsonObj);
        HttpClient.textBody(smsUrl).queryString("sign", MD5Util.getApolloMd5(params.toString()))
                .json(params)
                .asString();
    }
}
