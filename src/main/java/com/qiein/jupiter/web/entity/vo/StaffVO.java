package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;

public class StaffVO {
    private String id;

    private int compandyId;

    private String nickName;

    private boolean lockFlag;

    private String groupId;//小组ID
    private String phone;//手机号
    private String userName;//全名
    private String headImg;//头像
    private String passWord;//密码
    private int roleId;//角色ID
    private String roleName;//角色名
    private boolean pwdFlag;//密码是否默认密码

    /*获取员工是否初始密码标识，并置空密码*/
    public void getStaffPwdFlag() {
        if (StringUtil.isNotNullStr(this.phone) && StringUtil.isNotNullStr(this.passWord) && this.passWord.equals(MD5Util.getSaltMd5(this.phone))) {
            this.pwdFlag = true;
        } else {
            this.pwdFlag = false;
        }
        this.passWord = null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCompandyId() {
        return compandyId;
    }

    public void setCompandyId(int compandyId) {
        this.compandyId = compandyId;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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
}
