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
    private int companyId;
    private String headImg;
    private int statusFlag;
    private int todayNum;
    private int weight;
    private int limitDay;
    private int lastPushTime;
    private String limitChannelIds;
    private String limitChannelNames;
    private String limitShopIds;
    private String limitShopNames;
    private int lastLoginTime;
    private String lastLoginIp;
    private int lastLogoutTime;
    private String lastLogoutIp;
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public int getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(int todayNum) {
        this.todayNum = todayNum;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public int getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(int lastPushTime) {
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

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(int lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getLastLogoutIp() {
        return lastLogoutIp;
    }

    public void setLastLogoutIp(String lastLogoutIp) {
        this.lastLogoutIp = lastLogoutIp;
    }
}
