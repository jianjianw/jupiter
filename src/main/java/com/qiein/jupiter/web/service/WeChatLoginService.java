package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffDetailPO;

/**
 * wx登录
 *
 * @Author: shiTao
 */
public interface WeChatLoginService {

    /**
     * 根据CODE 获取access token
     * @return 
     */
    StaffDetailPO getAccessToken(String code);


    /**
     * 根据accessToken和 openId 获取用户信息
     * @return 
     */
    StaffDetailPO getUserInfo(String token, String openId);
}
