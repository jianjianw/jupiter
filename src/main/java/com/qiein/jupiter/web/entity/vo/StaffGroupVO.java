package com.qiein.jupiter.web.entity.vo;

/**
 * @author: yyx
 * @Date: 2018-8-4
 */
public class StaffGroupVO {
    /**
     * 员工Id
     * */
    private Integer staffId;

    /**
     * 分组个数
     * */
    private Integer groupCount;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }
}
