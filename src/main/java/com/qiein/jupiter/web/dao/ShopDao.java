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
     *
     * @param companyId
     * @return
     */
    List<ShopVO> getCompanyShopList(@Param("companyId") int companyId);

    /**
     * 拍摄地名称
     *
     * @param companyId
     * @param shopName
     * @return
     */
    ShopPO getShopByName(@Param("companyId") int companyId, @Param("shopName") String shopName);

    /**
     * 编辑拍摄地开关
     *
     * @param companyId
     * @param id
     * @param showFlag
     * @return
     */
    int editShopShowFlag(@Param("companyId") int companyId, @Param("id") int id, @Param("showFlag") boolean showFlag);

    /**
     * 删除拍摄地
     *
     * @param companyId
     * @param id
     * @return
     */
    int deleteShop(@Param("companyId") int companyId, @Param("id") int id);

    /**
     * 获取企业显示的拍摄地列表
     *
     * @param companyId
     * @return
     */
    List<ShopVO> getShowShopList(@Param("companyId") int companyId);

    /**
     * 根据ID，获取企业显示拍摄地信息
     *
     * @param companyId
     * @param id
     * @return
     */
    ShopVO getShowShopById(@Param("companyId") int companyId, @Param("id") int id);
}
