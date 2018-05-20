package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * @Author: shiTao
 */
public interface LoginService {

    /**
     * 微信 获取公司列表
     */
    List<CompanyPO> getCompanyListByWeChat(String weChatUnionID);

    /**
     * 微信 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByWeChat(String weChatUnionID, int companyId, String ip);

    /**
     * 手机号码 获取公司列表
     */
    List<CompanyPO> getCompanyListByPhone(String phone, String password);

    /**
     * 手机号码 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByPhone(String phone, String password, int companyId);

    /**
     * 钉钉 获取公司列表
     */
    List<CompanyPO> getCompanyListByDing(String dingUnionId);

    /**
     * 钉钉 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByDing(String dingUnionId, int companyId, String ip);
}
