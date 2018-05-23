package com.qiein.jupiter.util.wechat;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.entity.dto.WeChatAuthDTO;

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

    /**
     * 根据认证CODE 获取access token
     *
     * @param code
     * @return
     */
    public WeChatAuthDTO getAccessToken(String code) {
        String wechatRes = HttpClient
                .get(tokenUrl)
                .queryString("appid", appid)
                .queryString("secret", secret)
                .queryString("code", code)
                .queryString("grant_type", "authorization_code")
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        WeChatAuthDTO weChatAuthDTO = new WeChatAuthDTO();
        weChatAuthDTO.setAccessToken(res.getString("access_token"));
        weChatAuthDTO.setAuthCode(code);
        weChatAuthDTO.setOpenId(res.getString("openid"));
        return weChatAuthDTO;
    }

    /**
     * 获取用户信息
     *
     * @param weChatAuthDTO
     * @return
     */
    public WeChatAuthDTO getUserInfo(WeChatAuthDTO weChatAuthDTO) {
        String wechatRes = HttpClient
                .get(userInfoUrl)
                .queryString("access_token", weChatAuthDTO.getAccessToken())
                .queryString("openid", weChatAuthDTO.getOpenId())
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        weChatAuthDTO.setUnionId(res.getString("unionid"));
        weChatAuthDTO.setHeadImg(res.getString("headimgurl"));
        return weChatAuthDTO;

    }
}
