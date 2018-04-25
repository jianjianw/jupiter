package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * 获取手机号码归属地
 *
 * @author JingChenglong 2017-02-10 16:54
 */
public class MobileLocationUtil {

    /**
     * -- 获取省市信息逗号分隔 --
     **/
    public static String getPhoneLocation(String phone) {

        JSONObject json = null;
        String location = null;
        try {
            json = OkHttpUtil
                    .doGet("http://apis.juhe.cn/mobile/get?phone=" + phone + "&key=0332415596ab60d21168ba02d059fdbb");

            if (json.getString("resultcode").equals("200")) {
                String province = json.getJSONObject("result").getString("province");
                String city = json.getJSONObject("result").getString("city");
                location = province + "," + city + ",,";
            } else {
                location = "";
            }
        } catch (IOException e) {
            return "";
        }
        return location;
    }

    public static String getPhonePostcode(String phone) {

        JSONObject json = null;
        try {
            json = OkHttpUtil
                    .doGet("http://apis.juhe.cn/mobile/get?phone=" + phone + "&key=0332415596ab60d21168ba02d059fdbb");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        String postcode = "";
        if (json.getString("resultcode").equals("200")) {
            postcode = json.getJSONObject("result").getString("zip");
        }
        return postcode;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getPhoneLocation("13221009948"));
    }

    /**
     * 根据联系方式获取地址
     *
     * @param phone
     * @param wechat
     * @param qq
     * @return
     */
    public static String getAddressByContactInfo(String phone, String wechat, String qq) {
        // 如果地址信息为空，则取客人手机归属地信息作为地址
        if (StringUtil.isNotEmpty(phone)) {
            return getPhoneLocation(phone);
        } else if (StringUtil.isNotEmpty(wechat)) {
            return getPhoneLocation(wechat);
        } else if (StringUtil.isNotEmpty(qq)) {
            return getPhoneLocation(qq);
        }
        return "";
    }
}