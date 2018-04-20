package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 拍摄地渠道小组关联
 */
public class ShopChannelGroupPO extends BaseEntity {
    /**
     * 拍摄地
     */
    private int shopId;
    /**
     * 渠道名
     */
    private int channelId;
    /**
     * 小组ID
     */
    private String groupId;
    /**
     * 权重
     */
    private int weight;
    /**
     * 企业ID
     */
    private int companyId;

    public ShopChannelGroupPO() {
    }

    public ShopChannelGroupPO(int shopId, int channelId, String groupId, int weight, int companyId) {
        this.shopId = shopId;
        this.channelId = channelId;
        this.groupId = groupId;
        this.weight = weight;
        this.companyId = companyId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
