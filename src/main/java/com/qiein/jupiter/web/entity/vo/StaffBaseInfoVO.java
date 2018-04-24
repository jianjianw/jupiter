package com.qiein.jupiter.web.entity.vo;


import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.PermissionPO;

import java.io.Serializable;
import java.util.List;

/**
 * 员工基础信息
 */
public class StaffBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 9076352385052021856L;
    /**
     * 员工权限信息
     */
    private List<PermissionPO> permission;
    /**
     * 员工详细信息
     */
    private StaffDetailVO staffDetail;
    /**
     * 员工所在小组的信息
     */
    private List<GroupPO> group;
    /**
     * 公司
     */
    private CompanyVO company;

    public List<GroupPO> getGroup() {
        return group;
    }

    public void setGroup(List<GroupPO> group) {
        this.group = group;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<PermissionPO> getPermission() {
        return permission;
    }

    public void setPermission(List<PermissionPO> permission) {
        this.permission = permission;
    }

    public StaffDetailVO getStaffDetail() {
        return staffDetail;
    }

    public void setStaffDetail(StaffDetailVO staffDetail) {
        this.staffDetail = staffDetail;
    }

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(CompanyVO company) {
        this.company = company;
    }
}
