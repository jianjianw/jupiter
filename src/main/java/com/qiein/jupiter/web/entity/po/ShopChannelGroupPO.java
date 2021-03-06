package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 拍摄地渠道小组关联
 */
public class ShopChannelGroupPO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 拍摄地
	 */
	private int typeId;

	/**
	 * 渠道名
	 */
	private int channelId;

	/**
	 * 小组ID
	 */
	private String groupId;

	/**
	 * 小组名
	 */
	private String groupName;

	/**
	 * 权重
	 */
	private int weight = 1;

	/**
	 * 今日领取客资数
	 */
	private int todayNum;

	/**
	 * 领取客资差比
	 */
	private double diffPid = Double.MAX_VALUE;

	/**
	 * 企业ID
	 */
	private int companyId;

	public ShopChannelGroupPO() {
	}

	public ShopChannelGroupPO(int typeId, int channelId, String groupId, String groupName, int weight, int companyId) {
		this.typeId = typeId;
		this.channelId = channelId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.weight = weight;
		this.companyId = companyId;
	}

	/**
	 * 差比计算
	 */
	public void doCalculateAllotNumDiffPID() {
		if (this.weight == 0) {
			this.weight = 1;
		}
		double w = this.weight;
		this.diffPid = (this.weight - this.todayNum) / w;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getTodayNum() {
		return todayNum;
	}

	public void setTodayNum(int todayNum) {
		this.todayNum = todayNum;
	}

	public double getDiffPid() {
		return diffPid;
	}

	public void setDiffPid(double diffPid) {
		this.diffPid = diffPid;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}