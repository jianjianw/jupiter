package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 组数据库对象
 */
public class GroupPO extends BaseEntity {

    private static final long serialVersionUID = -3838894504630445398L;
    /**
     * 组id
     */
    @NotEmpty(message = "{group.id.null}")
    private String groupId;
    /**
     * 组名称
     */
    @NotEmpty(message = "{group.name.null}")
    private String groupName;
    /**
     * 父级id
     */
    @NotEmpty(message = "{group.parentId.null}")
    private String parentId;
    /**
     * 排序
     */
    private int priority;
    /**
     * 组类型
     */
    private String groupType;
    /**
     * 公司ID
     */
    private int companyId;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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
}
