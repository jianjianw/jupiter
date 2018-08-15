package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ShopChannelGroupPO;
import com.qiein.jupiter.web.entity.vo.ChannelGroupVO;
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
    List<ShopChannelGroupVO> getShopChannelGroupList(@Param("companyId") int companyId, @Param("shopId") int shopId);

    /**
     * 编辑小组权重
     *
     * @param companyId
     * @param relaId
     * @return
     */
    int editGroupWeight(@Param("companyId") int companyId, @Param("relaId") int relaId, @Param("weight") int weight);

    /**
     * 批量删除拍摄地渠道小组关联
     *
     * @param companyId
     * @param relaIdArr
     */
    void batchDeleteGroup(@Param("companyId") int companyId, @Param("relaIdArr") String[] relaIdArr);

    /**
     * 根据渠道拍摄地，删除记录
     *
     * @param companyId
     * @param shopId
     * @param channelId
     */
    void deleteByChannelAndShopId(@Param("companyId") int companyId, @Param("channelId") int channelId,
                                  @Param("shopId") int shopId);

    /**
     * 根据渠道编号，删除渠道下所有拍摄地关联信息记录
     *
     * @param companyId
     * @param channelId
     * @return
     */
    int delByChannelId(@Param("companyId") int companyId, @Param("channelId") int channelId);

    /**
     * 批量添加拍摄地渠道关联
     *
     * @param list
     */
    void batchAddShopChannel(List<ShopChannelGroupPO> list);

    /**
     * 根据拍摄地，渠道，小组查询关联
     *
     * @param companyId
     * @param channelId
     * @param shopId
     * @param groupId
     * @return
     */
    ShopChannelGroupPO getByShopAndChannelAndGroup(@Param("companyId") int companyId, @Param("channelId") int channelId,
                                                   @Param("shopId") int shopId, @Param("groupId") String groupId);

    /**
     * 编辑客服小组
     *
     * @param companyId
     * @param relaId
     * @param groupId
     * @return
     */
    int editChannelGroup(@Param("companyId") int companyId, @Param("relaId") int relaId,
                         @Param("groupId") String groupId, @Param("groupName") String groupName);

    /**
     * 模糊查询渠道小组关联
     *
     * @param companyId
     * @param shopId
     * @param channelId
     * @param searchKey
     * @return
     */
    List<ChannelGroupVO> searchChannelGroup(@Param("companyId") int companyId, @Param("shopId") int shopId,
                                            @Param("channelId") int channelId, @Param("searchKey") String searchKey);

    /**
     * 客服组修改名字时联动修改关联分配表中的名字
     *
     * @param groupName
     * @param groupId
     * @param companyId
     */
    void updateGroupNameById(@Param("groupName") String groupName, @Param("groupId") String groupId,
                             @Param("companyId") int companyId);

    /**
     * 根据渠道和拍摄类型获取关联的客服组集合
     *
     * @param companyId
     * @param typeId
     * @param channelId
     * @return
     */
    List<ShopChannelGroupPO> listShopChannelGroupRela(@Param("companyId") int companyId, @Param("typeId") int typeId,
                                                      @Param("channelId") int channelId, @Param("infoTab") String infoTab);

    /**
     * 当一个组被删除时，需要把对应的关联设置删掉
     */
    int delByGroupId(@Param("companyId") int companyId, @Param("groupId") String groupId);
}