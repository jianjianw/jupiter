package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 角色权限VO对象
 */
public class RolePermissionVO extends BaseEntity {
    private static final long serialVersionUID = -9006130668581698213L;

    /**
     * 角色Id
     */
    private int roleId;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 排序
     */
    private int priority;

    /**
     * 权限id
     */
    private int permissionId;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型
     */
    private String typeId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
