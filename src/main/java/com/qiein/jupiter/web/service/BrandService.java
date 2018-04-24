package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.BrandPO;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24 0024.
 */
public interface BrandService {
    /**
     * 新增品牌
     *
     * @param brandPO
     */
    void createBrand(BrandPO brandPO);

    /**
     * 删除品牌
     *
     * @param id
     */
    void delBrand(Integer id, Integer companyId);

    /**
     * 编辑品牌
     *
     * @param brandPO
     */
    void editBrand(BrandPO brandPO);

    /**
     * 获取品牌列表
     *
     * @param companyId
     * @return
     */
    List<BrandPO> getBrandList(Integer companyId);
}