package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class BaseStaffVO implements Serializable {
    private static final long serialVersionUID = -5144309307374290131L;
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
    /**
     * 员工状态
     */
    private int statusFlag;
    /**
     * 是否锁定
     */
    private boolean lockFlag;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public boolean getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }
}
