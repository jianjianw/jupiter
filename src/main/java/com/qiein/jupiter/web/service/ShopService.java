package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ShopVO;

import java.util.List;

/**
 * 拍摄地（门店）
 */
public interface ShopService {
    /**
     * 获取企业所有拍摄地列表
     *
     * @param companyId
     * @return
     */
    public List<ShopVO> getCompanyShopList(int companyId);
}
