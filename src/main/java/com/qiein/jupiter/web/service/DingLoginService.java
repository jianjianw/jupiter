package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffDetailPO;

/**
 * 钉钉登录
 *
 * @Author: shiTao
 */
public interface DingLoginService {

    /**
     * 定时根据appid 和secreit 获取应用token
     */
    void timingGetAccessToken();

    /**
     * 根据用户登录授权码和access token 获取用户持久授权码（包含openid 和 unionid）
     */
    void getPersistentCode(String authCode);

    /**
     * 根据access token 和 openid 。persistent_code 获取用户sns token;
     */
    void getSnsToken(String accessToken, String openId, String persistentCode);

    /**
     * 根据  sns token 获取用户信息
     */
    void getUserInfo(String snsToken);
}
