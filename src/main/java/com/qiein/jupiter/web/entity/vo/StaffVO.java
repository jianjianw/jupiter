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
     * 小组id(old)
     * */
    private String oldGroupId;
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
    private int statusFlag;

    /**
     * 是否删除
     */
    private boolean delFlag;
    /**
     * 每日接单限额
     */
    private int limitDay;
    /**
     * 按钮标记
     * 0--删除  1---移除
     * */
    private int delBtFlag;

    /**
     * 角色集合
     */
    private List<RoleVO> roleList;

    public String getOldGroupId() {
        return oldGroupId;
    }

    public void setOldGroupId(String oldGroupId) {
        this.oldGroupId = oldGroupId;
    }

    /**
     * 获取员工是否初始密码标识，并置空密码
     */
    public void getStaffPwdFlag() {
        this.pwdFlag = StringUtil.isNotEmpty(this.getPhone())
                && StringUtil.isNotEmpty(this.getPassword())
                && this.getPassword().equals(MD5Util.getSaltMd5(this.getPhone()));
    }

    public StaffVO() {
    }

    public StaffVO(int id, int companyId, int roleId) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.roleId = roleId;
    }

    public int getDelBtFlag() {
        return delBtFlag;
    }

    public void setDelBtFlag(int delBtFlag) {
        this.delBtFlag = delBtFlag;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    @Override
    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }
}
