package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.PageConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 页面配置
 */
public interface PageConfigDao {
    /**
     * 根据公司ID 获取配置
     *
     * @param cid
     * @return
     */
    List<PageConfig> listPageConfigByCidAndRole(@Param("companyId") int cid, @Param("role") String role);

    /**
     *根据公司ID 获取 显示的配置
     *
     * @param cid
     * @return
     */
    List<PageConfig> listIsShowPageConfigByCidAndRole(@Param("companyId") int cid, @Param("role") String role);

    /**
     * 保存页面配置
     *
     * @param pageConfig
     * @return
     */
    int updatePageConfig(PageConfig pageConfig);
}
