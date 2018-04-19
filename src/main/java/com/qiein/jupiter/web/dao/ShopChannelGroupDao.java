package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ShopChannelGroupPO;
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

    /**
     * 编辑小组权重
     *
     * @param companyId
     * @param relaId
     * @return
     */
    public int editGroupWeight(@Param("companyId") int companyId, @Param("relaId") int relaId, @Param("weight") int weight);

    /**
     * 批量删除拍摄地渠道小组关联
     *
     * @param companyId
     * @param relaIdArr
     */
    public void batchDeleteGroup(@Param("companyId") int companyId, @Param("relaIdArr") String[] relaIdArr);

    /**
     * 根据渠道拍摄地，删除记录
     *
     * @param companyId
     * @param shopId
     * @param channelId
     */
    public void deleteByChannelAndShopId(@Param("companyId") int companyId, @Param("channelId") int channelId, @Param("shopId") int shopId);

    /**
     * 批量添加拍摄地渠道关联
     *
     * @param list
     */
    public void batchAddShopChannel(List<ShopChannelGroupPO> list);

    /**
     * 根据拍摄地，渠道，小组查询关联
     *
     * @param companyId
     * @param channelId
     * @param shopId
     * @param groupId
     * @return
     */
    public ShopChannelGroupPO getByShopAndChannelAndGroup(@Param("companyId") int companyId, @Param("channelId") int channelId,
                                                          @Param("shopId") int shopId, @Param("groupId") String groupId);

    /**
     * 编辑客服小组
     *
     * @param companyId
     * @param relaId
     * @param groupId
     * @return
     */
    public int editChannelGroup(@Param("companyId") int companyId, @Param("relaId") int relaId, @Param("groupId") String groupId);
}
