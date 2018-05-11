package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.PageFilterDTO;
import com.qiein.jupiter.web.entity.po.PageConfig;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取公司所有的表格过滤
     *
     * @param companyId
     * @return
     */
    Map<String, List<PageFilterDTO>> getAllPageFilterMap(int companyId);
}
