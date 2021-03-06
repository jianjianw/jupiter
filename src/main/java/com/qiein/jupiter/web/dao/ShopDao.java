package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.ShopTargetDTO;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import org.apache.ibatis.annotations.Param;

import javax.annotation.security.PermitAll;
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
    List<ShopPO> getCompanyShopList(@Param("companyId") int companyId);

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

    /**
     * 批量根据拍摄地id获取拍摄地名列表
     *
     * @param ids
     * @param companyId
     * @return
     */
    String getLimitShopNamesByIds(@Param("ids") String[] ids, @Param("companyId") int companyId);

    /**
     * 获取所在小组承接拍摄地
     *
     * @param companyId
     * @param groupId
     * @return
     */
    List<ShopPO> getShopListByStaffGroup(@Param("companyId") int companyId, @Param("groupId") String groupId);

    /**
     * 根据id获取门店信息
     *
     * @param id
     * @return
     */
    ShopPO findShop(@Param("id") Integer id);

    /**
     * 获取门店门市列表
     *
     * @param companyId
     * @return
     */
    List<ShopVO> getShopAndStaffList(@Param("companyId") int companyId);

    /**
     * 校验是否存在该日期内该门市的目标
     * @param companyId
     * @param time
     * @return
     */
    List<ShopTargetDTO> findShopTarget(@Param("companyId")int companyId,@Param("time")int time,@Param("shopId")int shopId,@Param("type")int type);

    /**
     * 修改门市目标
     * @param shopTargetDTO
     */
    void editTarget(ShopTargetDTO shopTargetDTO);
    /**
     * 增加门市目标
     */
    void insertTarget(ShopTargetDTO shopTargetDTO);
}
