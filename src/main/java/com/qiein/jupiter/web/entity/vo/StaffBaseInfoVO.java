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
    private StaffPermissionVO staffPermissionVO;

    /**
     * 权限集合
     */
    private Map<String,Integer> permissionMap;

    /**
     * 公司
     */
    private CompanyPO companyPO;

    public StaffPermissionVO getStaffPermissionVO() {
        return staffPermissionVO;
    }

    public void setStaffPermissionVO(StaffPermissionVO staffPermissionVO) {
        this.staffPermissionVO = staffPermissionVO;
    }

    public Map<String, Integer> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, Integer> permissionMap) {
        this.permissionMap = permissionMap;
    }

    public CompanyPO getCompanyPO() {
        return companyPO;
    }

    public void setCompanyPO(CompanyPO companyPO) {
        this.companyPO = companyPO;
    }
}
