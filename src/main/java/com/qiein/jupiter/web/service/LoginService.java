package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.BaseInfoVO;
import com.qiein.jupiter.web.entity.vo.LoginUserVO;

import java.util.List;

/**
 * 登录服务
 *
 * @Author: shiTao
 */
public interface LoginService {


    /**
     * 微信 获取公司列表
     */
    List<CompanyPO> getCompanyListByWeChat(String authCode);

    /**
     * 微信 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByWeChat(String authCode, int companyId, String ip);

    /**
     * 手机号码 获取公司列表
     */
    List<CompanyPO> getCompanyListByPhone(LoginUserVO loginUserVO);

    /**
     * 手机号码 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByPhone(LoginUserVO loginUserVO);

    /**
     * 钉钉 获取公司列表
     */
    List<CompanyPO> getCompanyListByDing(String authCode);

    /**
     * 钉钉 根据公司ID 登录
     */
    StaffPO loginWithCompanyIdByDing(String authCode, int companyId, String ip);

    /**
     * 获取基础信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    BaseInfoVO getBaseInfo(int staffId, int companyId);

    /**
     * 更新token
     *
     * @param staffPO
     */
    String updateToken(StaffPO staffPO);
}
