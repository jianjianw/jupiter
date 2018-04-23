package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * staff vo
 */
public class StaffVO extends StaffPO {

    private static final long serialVersionUID = -4899607202977271346L;
    /**
     * 小组ID
     */
    private String groupId;

    /**
     * 角色ID
     */
    private int roleId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 密码是否默认密码
     */
    private boolean pwdFlag;
    /**
     * 角色ID，逗号拼接
     */
    private String roleIds;

    /**
     * 是否在线
     */
    private int showFlag;

    /**
     * 是否删除
     */
    private boolean delFlag;

    /**
     * 角色集合
     */
    private List<RoleVO> roleList;


    /**
     * 获取员工是否初始密码标识，并置空密码
     */
    public void getStaffPwdFlag() {
        this.pwdFlag = StringUtil. isEmpty(this.getPhone())
                && StringUtil. isEmpty(this.getPassword())
                && this.getPassword().equals(MD5Util.getSaltMd5(this.getPhone()));
    }

    public StaffVO() {
    }

    public StaffVO(int id, int companyId, int roleId) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.roleId = roleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public boolean isPwdFlag() {
        return pwdFlag;
    }

    public void setPwdFlag(boolean pwdFlag) {
        this.pwdFlag = pwdFlag;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public int getShowFlag() {
        return showFlag;
    }

    @Override
    public void setShowFlag(int showFlag) {
        this.showFlag = showFlag;
    }

    @Override
    public boolean isDelFlag() {
        return delFlag;
    }

    @Override
    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public List<RoleVO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleVO> roleList) {
        this.roleList = roleList;
    }
}
