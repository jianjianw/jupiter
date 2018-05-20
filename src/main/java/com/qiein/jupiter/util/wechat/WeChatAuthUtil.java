package com.qiein.jupiter.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import org.springframework.stereotype.Component;

/**
 * 微信验证工具类
 *
 * @Author: shiTao
 */
@Component
public class WeChatAuthUtil {
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


    public String getAccessToken(String code) {
        String wechatRes = HttpClient
                .get(tokenUrl)
                .queryString("appid", appid)
                .queryString("secret", secret)
                .queryString("code", code)
                .queryString("grant_type", "authorization_code")
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        return res.getString("openid");
    }

    public StaffDetailPO getUserInfo(String token, String openId) {
        StaffDetailPO staffDetailPO = new StaffDetailPO();
        staffDetailPO.setWeChatOpenId(openId);
        String wechatRes = HttpClient
                .get(userInfoUrl)
                .queryString("access_token", token)
                .queryString("openid", openId)
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
        staffDetailPO.setWeChatName(res.getString("nickname"));
        staffDetailPO.setWeChatImg(res.getString("headimgurl"));
        return staffDetailPO;

    }
}
