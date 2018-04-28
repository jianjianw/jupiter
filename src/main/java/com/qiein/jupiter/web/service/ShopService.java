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
     * 修改排序
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @param companyId
     */
    void editPriority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId);

    /**
     * 编辑拍摄地开关
     *
     * @param companyId
     * @param id
     * @param showFlag
     * @return
     */
    public void editShopShowFlag(int companyId, int id, boolean showFlag);

    /**
     * 删除拍摄地
     *
     * @param companyId
     * @param id
     */
    public void deleteShop(int companyId, int id);

    /**
     * 获取企业显示的拍摄地列表
     *
     * @param companyId
     * @return
     */
    List<ShopVO> getShowShopList(int companyId);
}
