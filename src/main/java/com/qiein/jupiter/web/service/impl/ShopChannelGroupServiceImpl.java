package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.entity.po.ShopChannelGroupPO;
import com.qiein.jupiter.web.entity.vo.ChannelGroupVO;
import com.qiein.jupiter.web.entity.vo.ShopChannelGroupVO;
import com.qiein.jupiter.web.service.ShopChannelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ShopChannelGroupServiceImpl implements ShopChannelGroupService {

    @Autowired
    private ShopChannelGroupDao shopChannelGroupDao;//拍摄地，渠道，小组关联

    /**
     * 获取拍摄地，渠道，小组关联集合
     *
     * @param companyId
     * @param shopId
     * @return
     */
    public List<ShopChannelGroupVO> getShopChannelGroupList(int companyId, int shopId) {
        return shopChannelGroupDao.getShopChannelGroupList(companyId, shopId);
    }

    /**
     * 编辑小组权重
     *
     * @param companyId
     * @param relaId
     * @return
     */
    public void editGroupWeight(int companyId, int relaId, int weight) {
        shopChannelGroupDao.editGroupWeight(companyId, relaId, weight);
    }

    /**
     * 批量删除拍摄地渠道小组关联
     *
     * @param companyId
     * @param relaIdIds
     */
    public void batchDeleteGroup(int companyId, String relaIdIds) {
        String[] relaIdArr = relaIdIds.split(CommonConstant.STR_SEPARATOR);
        shopChannelGroupDao.batchDeleteGroup(companyId, relaIdArr);
    }

    /**
     * 删除拍摄地渠道关联
     *
     * @param companyId
     * @param channelId
     */
    public void deleteChannelList(int companyId, int channelId, int shopId) {
        shopChannelGroupDao.deleteByChannelAndShopId(companyId, channelId, shopId);
    }

    /**
     * 批量添加拍摄地渠道关联
     *
     * @param companyId
     * @param shopId
     * @param weight
     * @param channelIds
     * @param groupIds
     */
    public void batchAddChannelList(int companyId, int shopId, int weight, String channelIds, String groupIds) {
        String[] channelIdArr = channelIds.split(CommonConstant.STR_SEPARATOR);
        String[] groupIdArr = groupIds.split(CommonConstant.STR_SEPARATOR);
        List<ShopChannelGroupPO> list = new LinkedList<>();
        for (String channel : channelIdArr) {
            for (String groupId : groupIdArr) {
                //查重
                ShopChannelGroupPO exist = shopChannelGroupDao.getByShopAndChannelAndGroup(companyId, Integer.parseInt(channel), shopId, groupId);
                if (exist != null) {
                    throw new RException(ExceptionEnum.CHANNEL_GROUP_EXIST);
                }
                ShopChannelGroupPO po = new ShopChannelGroupPO(shopId, Integer.parseInt(channel), groupId, weight, companyId);
                list.add(po);
            }
        }
        //批量添加
        shopChannelGroupDao.batchAddShopChannel(list);
    }

    /**
     * 编辑关联客服小组
     *
     * @param companyId
     * @param relaId
     * @param groupId
     */
    public void editChannelGroup(int relaId, int companyId, int channelId, int shopId, String groupId) {
        //1.查重
        ShopChannelGroupPO exist = shopChannelGroupDao.getByShopAndChannelAndGroup(companyId, channelId, shopId, groupId);
        if (exist != null && exist.getId() != relaId) {
            throw new RException(ExceptionEnum.CHANNEL_GROUP_EXIST);
        }
        //2.修改groupId
        shopChannelGroupDao.editChannelGroup(companyId, relaId, groupId);
    }

    /**
     * 模糊查询渠道小组关联
     *
     * @param companyId
     * @param shopId
     * @param channelId
     * @param searchKey
     * @return
     */
    public List<ChannelGroupVO> searchChannelGroup(int companyId, int shopId, int channelId, String searchKey) {
        return shopChannelGroupDao.searchChannelGroup(companyId, shopId, channelId, searchKey);
    }
}
