package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * wx登录
 *
 * @Author: shiTao
 */
public interface WeChatLoginService {

    /**
     * 根据CODE 获取access token
     *
     * @return StaffDetailPO
     */
    StaffDetailPO getAccessToken(String code);


    /**
     * 根据accessToken和 openId 获取用户信息
     *
     * @return StaffDetailPO
     */
    StaffDetailPO getUserInfo(String token, String openId);

    /**
     * 根据accessToken和 openId 获取用户信息
     *
     * @return
     */
    List<CompanyPO> forLogin(String code);


    StaffPO getCodeForIn(String code, String openid, int companyId, String ip);


}
