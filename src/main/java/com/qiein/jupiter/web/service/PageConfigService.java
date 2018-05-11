package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.PageConfig;
import com.qiein.jupiter.web.entity.vo.FilterMapVO;

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

    /**
     * 获取公司所有的表格过滤
     *
     * @param companyId
     * @return
     */
    FilterMapVO getPageFilterMap(int companyId, String role);
}
