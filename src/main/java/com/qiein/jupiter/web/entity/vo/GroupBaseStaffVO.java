package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 小组人员实体类
 */
public class GroupBaseStaffVO implements Serializable {
    /**
     * 小组ID
     */
    private String groupId;
    /**
     * 小组名称
     */
    private String groupName;
    /**
     * 是否本组
     */
    private boolean selectFlag;
    /**
     * 小组下的员工
     */
    private List<BaseStaffVO> staffList;

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

    public List<BaseStaffVO> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<BaseStaffVO> staffList) {
        this.staffList = staffList;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }
}
