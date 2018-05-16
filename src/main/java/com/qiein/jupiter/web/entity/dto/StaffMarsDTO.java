package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 网销排班员工实体类 Created by Administrator on 2018/5/10 0010.
 */
public class StaffMarsDTO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 全名
	 */
	private String userName;
	/**
	 * 所属公司编号
	 */
	private Integer companyId;
	/**
	 * 头像图片地址
	 */
	private String headImg;
	/**
	 * 员工状态标识
	 */
	private Integer statusFlag;
	/**
	 * 今日接单数
	 */
	private Integer todayNum;
	/**
	 * 权重
	 */
	private Integer weight;
	/**
	 * 今日上线
	 */
	private Integer limitDay;
	/**
	 * 最后推送时间
	 */
	private Integer lastPushTime;
	/**
	 * 不接受的渠道编号
	 */
	private String limitChannelIds;
	/**
	 * 不接受的渠道名称
	 */
	private String limitChannelNames;
	/**
	 * 不接受的拍摄地编号
	 */
	private String limitShopIds;
	/**
	 * 不接受的拍摄地名称
	 */
	private String limitShopNames;
	/**
	 * 最后登录时间
	 */
	private Integer lastLoginTime;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	/**
	 * 最后登出时间
	 */
	private Integer lastLogoutTime;
	/**
	 * 最后登出ip
	 */
	private String lastLogoutIp;
	/**
	 * 员工所属小组
	 */
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

    @Override
    public String toString() {
        return "StaffMarsDTO{" +
                "nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", userName='" + userName + '\'' +
                ", companyId=" + companyId +
                ", headImg='" + headImg + '\'' +
                ", statusFlag=" + statusFlag +
                ", todayNum=" + todayNum +
                ", weight=" + weight +
                ", limitDay=" + limitDay +
                ", lastPushTime=" + lastPushTime +
                ", limitChannelIds='" + limitChannelIds + '\'' +
                ", limitChannelNames='" + limitChannelNames + '\'' +
                ", limitShopIds='" + limitShopIds + '\'' +
                ", limitShopNames='" + limitShopNames + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLogoutTime=" + lastLogoutTime +
                ", lastLogoutIp='" + lastLogoutIp + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
