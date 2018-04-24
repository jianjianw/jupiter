package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class BaseStaffVO implements Serializable {
    /**
     * 员工ID
     */
    private int staffId;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 是否可勾选
     */
    private boolean selectFlag;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }
}
