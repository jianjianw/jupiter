package com.qiein.jupiter.web.entity.po;


import java.io.Serializable;

/**
 * 员工角色表
 */
public class StaffRolePO implements Serializable {
    private static final long serialVersionUID = 5878036122040487447L;
    /**
     * 员工id
     */
    private int staffId;

    /**
     * 角色id
     */
    private int roleId;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
