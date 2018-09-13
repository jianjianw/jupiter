package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 角色
 */
public class RoleVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	private int roleId;

	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 公司id
	 */
	private int companyId;
	/**
	 * 公司id
	 */
	private String pmsIds;

	/**
	 * 渠道ids
	 */
	private String sourceIds;

	public String getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(String sourceIds) {
		this.sourceIds = sourceIds;
	}

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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getPmsIds() {
		return pmsIds;
	}

	public void setPmsIds(String pmsIds) {
		this.pmsIds = pmsIds;
	}
}
