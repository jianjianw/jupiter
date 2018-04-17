package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拍摄地Dao（门店）
 */
public interface ShopDao extends BaseDao<ShopPO> {
    /**
     * 获取企业所有拍摄地列表
     * @param companyId
     * @return
     */
    public List<ShopVO> getCompanyShopList(@Param("companyId") int companyId);
}
