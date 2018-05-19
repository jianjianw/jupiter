package com.qiein.jupiter.web.service;

/**
 * wx登录
 *
 * @Author: shiTao
 */
public interface WeChatLoginService {

    /**
     * 根据CODE 获取access token
     */
    void getAccessToken(String code);


    /**
     * 根据accessToken和 openId 获取用户信息
     */
    void getUserInfo(String token, String openId);
}
