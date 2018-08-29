package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 搜索员工实体类
 * gaoxiaoli
 */
public class SearchStaffVO extends BaseEntity {
    private static final long serialVersionUID = -2875222976446719791L;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 全名
     */
    private String userName;
    /**
     * 小组ID
     */
    private String groupId;
    /**
     * 小组名称
     */
    private String groupName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 锁定标志
     */
    private boolean lockFlag;
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }
}
