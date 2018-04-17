package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ShopChannelGroupVO;

import java.util.List;

/**
 * 拍摄地，渠道，小组关联业务层
 */
public interface ShopChannelGroupService {

    /**
     * 获取拍摄地，渠道，小组关联集合
     *
     * @param companyId
     * @param shopId
     * @return
     */
    public List<ShopChannelGroupVO> getShopChannelGroupList(int companyId, int shopId);
}
