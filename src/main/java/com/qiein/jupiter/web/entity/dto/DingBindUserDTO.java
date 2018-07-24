package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 未绑定钉钉用户信息
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/24 10:56
 */
public class DingBindUserDTO extends BaseEntity {
    private int id;
    private String nickName;
    private String userName;
    private String phone;
    private String groupName;
    private int companyId;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
