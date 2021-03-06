package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * 员工权限vo
 */
public class StaffPermissionVO extends StaffPO {
    private static final long serialVersionUID = 4651773244566507603L;
    
    /**
     * 权限集合
     */
    private List<PermissionPO> permissionList;

    public List<PermissionPO> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionPO> permissionList) {
        this.permissionList = permissionList;
    }
}
