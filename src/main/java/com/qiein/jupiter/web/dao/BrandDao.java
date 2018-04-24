package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.BrandPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌Dao
 * Created by Administrator on 2018/4/24 0024.
 */
public interface BrandDao extends BaseDao<BrandPO>{

    /**
     * 检查是否存在同名品牌，如果有则返回大于等于1，如果没有返回0
     * @param brandName
     * @param companyId
     * @return
     */
    int checkBrandName(@Param("brandName") String brandName , @Param("companyId") Integer companyId);

    /**
     * 获取品牌列表
     * @param companyId
     * @return
     */
    List<BrandPO> getBrandList(@Param("companyId") Integer companyId);

}
