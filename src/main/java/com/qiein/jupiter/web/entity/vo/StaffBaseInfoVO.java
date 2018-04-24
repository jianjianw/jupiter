package com.qiein.jupiter.web.entity.vo;


import java.io.Serializable;
/**
 * 员工基础信息
 */
public class StaffBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 9076352385052021856L;
    /**
     * 员工基础信息
     */
    private StaffPermissionVO staffPermission;

    /**
     * 员工详细信息
     */
    private StaffDetailVO staffDetail;

    /**
     * 公司
     */
    private CompanyVO company;

    public StaffPermissionVO getStaffPermission() {
        return staffPermission;
    }

    public void setStaffPermission(StaffPermissionVO staffPermission) {
        this.staffPermission = staffPermission;
    }

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(CompanyVO company) {
        this.company = company;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public StaffDetailVO getStaffDetail() {
        return staffDetail;
    }

    public void setStaffDetail(StaffDetailVO staffDetail) {
        this.staffDetail = staffDetail;
    }
}
