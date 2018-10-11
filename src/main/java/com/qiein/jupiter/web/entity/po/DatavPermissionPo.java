package com.qiein.jupiter.web.entity.po;
/**
 * author HanJF
 * 管理员日志
 */
public class DatavPermissionPo {
	 /**
     * 角色Id
     */
    private Integer roleId;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 权限Id
     */
    private Integer permissionId;
    /**
     * 权限姓名
     */
    private String  permissionName;
    
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
   
 
}
