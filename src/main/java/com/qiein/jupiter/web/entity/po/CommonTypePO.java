package com.qiein.jupiter.web.entity.po;
/**
 * 拍摄类型渠道
 * Author xiangliang
 */
public class CommonTypePO {

    private Integer id;
    //拍摄类型id
    private Integer typeId;
    //拍摄类型名称
    private String typeName;
    //小组id
    private String groupId;
    //小组名称
    private String groupName;
    //渠道ids
    private String channelIds;
    //渠道id
    private Integer channelId;
    //公司id
    private Integer companyId;
    //权重
    private Integer weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
