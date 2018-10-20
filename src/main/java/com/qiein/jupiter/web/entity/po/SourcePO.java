package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 来源数据库对象
 */
public class SourcePO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 来源名称
     */
    @NotEmptyStr(message = "{Source.srcName.null}")
    private String srcName;

    /**
     * 来源图片地址
     */
    private String srcImg;

    /**
     * 来源类型： 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     */
    // @NotEmpty(message = "{Source.typeId.null}")
    private Integer typeId;

    /**
     * 来源所属渠道编号
     */
    // @NotEmpty(message = "{Source.channelId.null}")
    private Integer channelId;

    /**
     * 来源所属渠道名称
     */
    // @NotEmptyStr(message = "{Source.channelName.null}")
    private String channelName;

    /**
     * 来源所属品牌编号
     */
    // @NotEmpty(message = "{Source.brandId.null}")
    private Integer brandId;

    /**
     * 来源所属品牌名称
     */
    // @NotEmptyStr(message = "{Source.brandName.null}")
    private String brandName;

    /**
     * 所属公司编号
     */
    private Integer companyId;

    /**
     * 排序优先级
     */
    private Integer priority;

    /**
     * 是否开启筛选
     */
    // @NotEmpty(message = "{Source.isFilter.null}")
    private Boolean filterFlag;

    /**
     * 是否启用
     */
    // @NotEmpty(message = "{Source.isShow.null}")
    private Boolean showFlag;
    /**
     * 推送规则
     */
    private Integer pushRule;

    /**
     * 邀约id
     */
    private String linkIds;

    /**
     * 名称
     */
    private String nickNames;

    /**
     * 无参构造
     */
    public SourcePO() {

    }

    public SourcePO(String srcName, Integer typeId, Integer channelId, String channelName, Integer companyId,
                    Boolean showFlag, boolean filterFlag) {
        this.srcName = srcName;
        this.typeId = typeId;
        this.channelId = channelId;
        this.channelName = channelName;
        this.companyId = companyId;
        this.showFlag = showFlag;
        this.filterFlag = filterFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Boolean filterFlag) {
        this.filterFlag = filterFlag;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    public String getNickNames() {
        return nickNames;
    }

    public void setNickNames(String nickNames) {
        this.nickNames = nickNames;
    }


    public String getSrcName() {
        return srcName;
    }


    public String getLinkIds() {
        return linkIds;
    }

    public void setLinkIds(String linkIds) {
        this.linkIds = linkIds;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public Integer getPushRule() {
        return pushRule;
    }

    public void setPushRule(Integer pushRule) {
        this.pushRule = pushRule;
    }
}