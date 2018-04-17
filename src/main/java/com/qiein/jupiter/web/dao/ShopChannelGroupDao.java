package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.ShopChannelGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拍摄地，渠道，小组关联Dao
 */
public interface ShopChannelGroupDao {
    /**
     * 获取拍摄地，渠道，小组关联集合
     *
     * @param companyId
     * @param shopId
     * @return
     */
    public List<ShopChannelGroupVO> getShopChannelGroupList(@Param("companyId") int companyId, @Param("shopId") int shopId);
}
