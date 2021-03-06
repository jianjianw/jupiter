package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.qiein.jupiter.web.entity.po.PermissionPO;

/**
 * 角色权限VO对象
 */
public class RolePermissionVO implements Serializable {

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
	 * 企业ID
	 */
	private Integer companyId;
	/**
	 * 排序
	 */
	private int priority;
	/**
	 * 权限集合
	 */
	private List<PermissionPO> pmsList;

	/**
	 * 花费规则权限
	 */
	private List<RoleSourceVO> roleSourceList;

	public List<RoleSourceVO> getRoleSourceList() {
		return roleSourceList;
	}

	public void setRoleSourceList(List<RoleSourceVO> roleSourceList) {
		this.roleSourceList = roleSourceList;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<PermissionPO> getPmsList() {
		return pmsList;
	}

	public void setPmsList(List<PermissionPO> pmsList) {
		this.pmsList = pmsList;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}
