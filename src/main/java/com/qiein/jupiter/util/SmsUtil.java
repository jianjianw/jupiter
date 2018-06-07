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

    public static void sendSms(int companyId, String templateId, String phone, String param) {
        if (NumUtil.isInValid(companyId) || StringUtil.haveEmpty(templateId, phone) || !RegexUtil.checkMobile(phone)) {
            return;
        }
        HttpClient.post(smsUrl)
                .param("companyId", String.valueOf(companyId))
                .param("templateId", templateId)
                .param("phone", phone)
                .param("map", param)
                .execute();
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
        JSONObject param = new JSONObject();
        param.put("companyId", companyId);
        param.put("templateId", abnormalTemplateId);
        param.put("phone", phone);
        param.put("map", map);
        HttpClient.post(smsUrl)
                .param("params", param.toJSONString())
                .param("sign", MD5Util.getApolloMd5(param.toString()))
                .execute();
    }
}
