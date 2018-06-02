package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 在线时间封装类
 *
 * @author gaoxiaoli 2018/6/2
 */

public class OnLineTimePO extends BaseEntity {
    private static final long serialVersionUID = -1369747010643612987L;
    /**
     * 员工ID
     */
    private int staffId;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 在线时长
     */
    private int onLineTime;
    /**
     * 企业D
     */
    private int companyId;
    /**
     * 日期
     */
    private int lineDay;

    public OnLineTimePO() {
    }

    public OnLineTimePO(int staffId, String staffName, int companyId) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.companyId = companyId;
    }

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

    public int getOnLineTime() {
        return onLineTime;
    }

    public void setOnLineTime(int onLineTime) {
        this.onLineTime = onLineTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getLineDay() {
        return lineDay;
    }

    public void setLineDay(int lineDay) {
        this.lineDay = lineDay;
    }
}
