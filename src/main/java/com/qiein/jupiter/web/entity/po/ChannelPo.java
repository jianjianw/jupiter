package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;


/**
 * 渠道数据库对象
 */
public class ChannelPo extends BaseEntity {

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道图片地址
     */
    private String channelImg;

    /**
     * 渠道类型
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     */
    private Integer typeId;

    /**
     * 渠道所属品牌编号
     */
    private Integer brandId;

    /**
     * 渠道所属品牌名
     */
    private String brandName;

    /**
     * 渠道推送规则：
     * 0:不推送，1:自由领取，2:全员平均，3:小组平均，4:部门平均，5:...
     */
    private Integer pushRule;

    /**
     * 渠道所属企业编号
     */
    private Integer companyId; // 企业ID

    /**
     * 排序优先级
     */
    private Integer priority;

    /**
     * 是否启用 true启用/false停用
     */
    private Boolean isShow;


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelImg() {
        return channelImg;
    }

    public void setChannelImg(String channelImg) {
        this.channelImg = channelImg;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getPushRule() {
        return pushRule;
    }

    public void setPushRule(Integer pushRule) {
        this.pushRule = pushRule;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }
}
