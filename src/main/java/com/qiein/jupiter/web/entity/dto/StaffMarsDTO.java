package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 网销排班员工实体类
 * Created by Administrator on 2018/5/10 0010.
 */
public class StaffMarsDTO extends BaseEntity {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String phone;
    private String userName;
    private Integer companyId;
    private String headImg;
    private Integer statusFlag;
    private Integer todayNum;
    private Integer weight;
    private Integer limitDay;
    private Integer lastPushTime;
    private String limitChannelIds;
    private String limitChannelNames;
    private String limitShopIds;
    private String limitShopNames;
    private Integer lastLoginTime;
    private String lastLoginIp;
    private Integer lastLogoutTime;
    private String lastLogoutIp;
    private String groupId;

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(Integer todayNum) {
        this.todayNum = todayNum;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(Integer limitDay) {
        this.limitDay = limitDay;
    }

    public Integer getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(Integer lastPushTime) {
        this.lastPushTime = lastPushTime;
    }

    public String getLimitChannelIds() {
        return limitChannelIds;
    }

    public void setLimitChannelIds(String limitChannelIds) {
        this.limitChannelIds = limitChannelIds;
    }

    public String getLimitChannelNames() {
        return limitChannelNames;
    }

    public void setLimitChannelNames(String limitChannelNames) {
        this.limitChannelNames = limitChannelNames;
    }

    public String getLimitShopIds() {
        return limitShopIds;
    }

    public void setLimitShopIds(String limitShopIds) {
        this.limitShopIds = limitShopIds;
    }

    public String getLimitShopNames() {
        return limitShopNames;
    }

    public void setLimitShopNames(String limitShopNames) {
        this.limitShopNames = limitShopNames;
    }

    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Integer lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getLastLogoutIp() {
        return lastLogoutIp;
    }

    public void setLastLogoutIp(String lastLogoutIp) {
        this.lastLogoutIp = lastLogoutIp;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
