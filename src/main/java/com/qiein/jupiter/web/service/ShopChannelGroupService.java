package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ChannelGroupVO;
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

    /**
     * 编辑小组权重
     *
     * @param companyId
     * @param relaId
     * @return
     */
    public void editGroupWeight(int companyId, int relaId, int weight);

    /**
     * 批量删除拍摄地渠道小组关联
     *
     * @param companyId
     * @param relaIdIds
     */
    public void batchDeleteGroup(int companyId, String relaIdIds);

    /**
     * 删除拍摄地渠道关联
     *
     * @param companyId
     * @param channelId
     */
    public void deleteChannelList(int companyId, int channelId, int shopId);

    /**
     * 批量添加拍摄地渠道关联
     *
     * @param companyId
     * @param shopId
     * @param weight
     * @param channelIds
     * @param groupIds
     */
    public void batchAddChannelList(int companyId, int shopId, int weight, String channelIds, String groupIds);

    /**
     * 编辑关联客服小组
     *
     * @param companyId
     * @param relaId
     * @param groupId
     */
    public void editChannelGroup(int relaId, int companyId, int channelId, int shopId, String groupId);

    /**
     * 模糊查询渠道小组关联
     *
     * @param companyId
     * @param shopId
     * @param channelId
     * @param searchKey
     * @return
     */
    public List<ChannelGroupVO> searchChannelGroup(int companyId, int shopId, int channelId, String searchKey);
}
