package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.ShopPO;
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

    /**
     * 新增拍摄地
     *
     * @param shopPO
     */
    public void addShop(ShopPO shopPO);

    /**
     * 编辑拍摄地
     *
     * @param shopPO
     */
    public void editShop(ShopPO shopPO);

    /**
     * 编辑拍摄地开关
     *
     * @param companyId
     * @param id
     * @param showFlag
     * @return
     */
    public void editShopShowFlag(int companyId, int id, boolean showFlag);
}
