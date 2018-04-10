package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CompanyPO;

import java.util.List;
import java.util.Map;

/**
 * 公司实现接口
 */
public interface CompanyService {
    /**
     * 根据ID获取
     *
     * @param companyId
     * @return
     */
    CompanyPO getById(int companyId);

    /**
     * 根据条件获取
     *
     * @param map
     * @return
     */
    List<CompanyPO> findList(Map map);

    /**
     * 删除
     *
     * @param companyId
     * @return
     */
    int deleteFlag(int companyId);

    /**
     * 新增
     *
     * @param companyPO
     * @return
     */
    CompanyPO insert(CompanyPO companyPO);

    /**
     * 编辑
     *
     * @param companyPO
     * @return
     */
    int update(CompanyPO companyPO);

}
