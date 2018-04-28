package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * 客户端登录
 */
public interface ClientLoginService {
    /**
     * 获取公司的列表
     *
     * @param userName
     * @param password
     * @return
     */
    List<CompanyPO> getCompanyList(String userName, String password);

    /**
     * 根据公司id登录
     *
     * @param userName
     * @param password
     * @param companyId
     * @param ip
     * @return
     */
    StaffPO loginWithCompanyId(String userName, String password, int companyId, String ip);
}
