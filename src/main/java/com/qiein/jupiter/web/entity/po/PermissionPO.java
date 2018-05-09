package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 权限数据库对象
 */
public class PermissionPO implements Serializable {

	private static final long serialVersionUID = -8520233999874945394L;

	/**
	 * 权限id
	 */
	private int permissionId;

	/**
	 * 权限名称
	 */
	private String permissionName;

	/**
	 * 权限缩写
	 */
	private String abbreviate;

	/**
	 * 类型Id
	 */
	private int typeId;

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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getAbbreviate() {
		return abbreviate;
	}

	public void setAbbreviate(String abbreviate) {
		this.abbreviate = abbreviate;
	}
}
