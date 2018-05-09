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

	/**
	 * 企业ID
	 */
	private int companyId;

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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}
