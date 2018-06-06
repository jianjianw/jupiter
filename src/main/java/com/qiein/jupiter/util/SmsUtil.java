package com.qiein.jupiter.util;

import com.mzlion.easyokhttp.HttpClient;

import java.util.HashMap;

/**
 * 短信发送工具
 *
 * @author gaoxiaoli 2018/6/6
 */

public class SmsUtil {

    private static final String smsUrl = "http://114.55.249.156:8286/send_msg/send_msg";

    public static void sendSms(int companyId, String templateId, String phone, String param) {
        if (NumUtil.isInValid(companyId) || StringUtil.haveEmpty(templateId, phone) || !RegexUtil.checkMobile(phone)) {
            return;
        }
        if (!phone.equals("13567112749")) {
            return;
        }
        HttpClient.post(smsUrl)
                .param("companyId", String.valueOf(companyId))
                .param("templateId", templateId)
                .param("phone", phone)
                .param("map", param)
                .execute();
    }
}
