package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.service.WeChatLoginService;
import org.springframework.stereotype.Service;

/**
 * @Author: shiTao
 */
@Service
public class WeChatLoginServiceImpl implements WeChatLoginService {
    /**
     * 获取TOKEN的URL
     */
    private final static String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 获取用户信息的URL
     */
    private final static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
    /**
     * APPID
     */
    private final static String appid = "wx84a2df1a7858dc87";
    /**
     * SECRET
     */
    private final static String secret = "d3d9f6d809f9f03a01b2ecce5211264c";

    @Override
    public void getAccessToken(String code) {
        String wechatRes = HttpClient
                .get(userInfoUrl)
                .queryString("appid", appid)
                .queryString("secret", secret)
                .queryString("code", code)
                .queryString("grant_type", "authorization_code")
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
    }

    @Override
    public void getUserInfo(String token, String openId) {
        String wechatRes = HttpClient
                .get(tokenUrl)
                .queryString("access_token", token)
                .queryString("openid", openId)
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
    }
}
