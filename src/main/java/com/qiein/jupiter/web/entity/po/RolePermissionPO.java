package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 角色权限数据库对象
 */
public class RolePermissionPO extends BaseEntity {

    private static final long serialVersionUID = -333964837331076921L;
    /**
     * 角色id
     */
    private int roleId;
    /**
     * 权限id
     */
    private int permissionId;
    /**
     * 公司id
     */
    private int companyId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
