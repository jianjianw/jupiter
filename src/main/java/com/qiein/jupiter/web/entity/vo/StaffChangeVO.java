package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 员工交接VO
 * Created by Administrator on 2018/5/14 0014.
 */
public class StaffChangeVO implements Serializable {
    private Integer staffId;
    private Integer staffName;
    private Integer groupId;
    private Integer gourpName;
    private Integer beStaffId;
    private Integer companyId;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getStaffName() {
        return staffName;
    }

    public void setStaffName(Integer staffName) {
        this.staffName = staffName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGourpName() {
        return gourpName;
    }

    public void setGourpName(Integer gourpName) {
        this.gourpName = gourpName;
    }

    public Integer getBeStaffId() {
        return beStaffId;
    }

    public void setBeStaffId(Integer beStaffId) {
        this.beStaffId = beStaffId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
