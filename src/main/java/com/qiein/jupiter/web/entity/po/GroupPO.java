package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 组数据库对象
 */
public class GroupPO extends BaseEntity {

    private static final long serialVersionUID = -3838894504630445398L;
    /**
     * 组id
     */
//    @NotEmptyStr(message = "{group.id.null}")
    private String groupId;
    /**
     * 组名称
     */
    @NotEmptyStr(message = "{group.name.null}")
    private String groupName;
    /**
     * 父级id
     */
    @NotEmptyStr(message = "{group.parentId.null}")
    private String parentId;
    /**
     * 组类型
     */
    private String groupType;
    /**
     * 公司ID
     */
    private int companyId;

    /**
     * 主管Id
     */
    private String chiefIds;

    /**
     * 主管名称
     */
    private String chiefNames;
    /**
     * 门店id
     * */
    private Integer shopId;
    /**
     * 排序
     */
    private Integer priority;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getChiefIds() {
        return chiefIds;
    }

    public void setChiefIds(String chiefIds) {
        this.chiefIds = chiefIds;
    }

    public String getChiefNames() {
        return chiefNames;
    }

    public void setChiefNames(String chiefNames) {
        this.chiefNames = chiefNames;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
