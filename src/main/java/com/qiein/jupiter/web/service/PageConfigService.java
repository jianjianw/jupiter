package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.PageConfig;

import java.util.List;

/**
 * 页面配置
 */
public interface PageConfigService {

    /**
     * 根据供公司Id 和 角色获取配置
     *
     * @param companyId
     * @param role
     * @return
     */
    List<PageConfig> listPageConfigByCidAndRole(int companyId, String role);


    List<>
}
