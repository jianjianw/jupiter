package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.LoginService;

import java.util.List;

/**
 * @Author: shiTao
 */
public class LoginServiceImpl implements LoginService {

    /**
     * 微信获取公司列表
     *
     * @param weChatUnionID
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByWeChat(String weChatUnionID) {
        return null;
    }

    /**
     * 微信根据公司ID登录
     *
     * @param weChatUnionID
     * @param companyId
     * @param ip
     * @return
     */
    @Override
    public StaffPO loginWithCompanyIdByWeChat(String weChatUnionID, int companyId, String ip) {
        return null;
    }

    /**
     * 手机号码 获取公司列表
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByPhone(String phone, String password) {
        return null;
    }

    /**
     * 手机号码获取公司ID
     *
     * @param phone
     * @param password
     * @param companyId
     * @return
     */
    @Override
    public StaffPO loginWithCompanyIdByPhone(String phone, String password, int companyId) {
        return null;
    }

    /**
     * 钉钉获取公司列表
     *
     * @param dingUnionId
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByDing(String dingUnionId) {
        return null;
    }

    /**
     * 钉钉登录
     *
     * @param dingUnionId
     * @param companyId
     * @param ip
     * @return
     */
    @Override
    public StaffPO loginWithCompanyIdByDing(String dingUnionId, int companyId, String ip) {
        return null;
    }

    /**
     * 获取公司列表
     */
    private List<CompanyPO> getCompanyList(int staffId) {
        return null;
    }

    /**
     * 根据公司ID 登录
     */
    private StaffPO loginWithCompanyId(int staffId, int companyId, String ip) {
        return null;
    }
}
