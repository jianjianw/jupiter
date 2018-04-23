package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CompanyPO;

import java.util.Map;

/**
 * 员工基础信息
 */
public class StaffBaseInfoVO {
    /**
     * 员工基础信息
     */
    private StaffPermissionVO staffPermission;

    /**
     * 权限集合
     */
    private Map<String, Integer> permissionMap;

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

    public Map<String, Integer> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, Integer> permissionMap) {
        this.permissionMap = permissionMap;
    }

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(CompanyVO company) {
        this.company = company;
    }
}
