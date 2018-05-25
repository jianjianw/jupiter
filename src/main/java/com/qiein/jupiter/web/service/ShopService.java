package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.vo.ShopDictVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;

import java.util.List;
import java.util.Map;

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
    List<ShopVO> getCompanyShopList(int companyId);

    /**
     * 新增拍摄地
     *
     * @param shopPO
     */
    void addShop(ShopPO shopPO);

    /**
     * 编辑拍摄地
     *
     * @param shopPO
     */
    void editShop(ShopPO shopPO);

    /**
     * 修改排序
     *
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
    void editShopShowFlag(int companyId, int id, boolean showFlag);

    /**
     * 删除拍摄地
     *
     * @param companyId
     * @param id
     */
    void deleteShop(int companyId, int id);

    /**
     * 校验拍摄地是否可删除
     *
     * @param companyId
     * @param id
     */
    boolean shopCanDelete(int companyId, int id);


    /**
     * 获取企业显示的拍摄地列表
     *
     * @param companyId
     * @return
     */
    List<ShopVO> getShowShopList(int companyId);

    /**
     * 获取拍摄地的字典
     */
    Map<String, ShopDictVO> getShopDictByCid(int companyId);
}
