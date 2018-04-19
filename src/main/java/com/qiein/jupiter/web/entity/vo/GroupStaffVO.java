package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * 组-员工  对象
 */
public class GroupStaffVO {
    /**
     * 组id
     */
    private String groupId;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 父级菜单id
     */
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
     * 员工集合
     */
    private List<StaffPO> staffList;

    /**
     * 组集合
     */
    private List<GroupStaffVO> groupList;

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

    public List<StaffPO> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffPO> staffList) {
        this.staffList = staffList;
    }

    public List<GroupStaffVO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupStaffVO> groupList) {
        this.groupList = groupList;
    }
}
