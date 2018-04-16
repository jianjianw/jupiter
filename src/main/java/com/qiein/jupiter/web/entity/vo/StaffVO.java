package com.qiein.jupiter.web.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;

public class StaffVO {

    private int id;

    private int companyId;

    @NotEmpty(message = "{staff.name.null}")
    private String nickName;


    /**
     * 手机号码
     */
    @NotEmpty(message = "{staff.phone.null}")
    private String phone;
    /**
     * 全名
     */
    @NotEmpty(message = "{staff.userName.null}")
    private String userName;
    /**
     * 密码
     */
    private String password;

    private boolean lockFlag;
    /**
     * 小组ID
     */
    private String groupId;

    /**
     * 头像
     */
    private String headImg;

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
     * 获取员工是否初始密码标识，并置空密码
     */
    public void getStaffPwdFlag() {
        if (StringUtil.isNotNullStr(this.phone) && StringUtil.isNotNullStr(this.password) && this.password.equals(MD5Util.getSaltMd5(this.phone))) {
            this.pwdFlag = true;
        } else {
            this.pwdFlag = false;
        }
        this.password = null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(int showFlag) {
        this.showFlag = showFlag;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }
}
