package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 员工信息
 * 
 * @author JingChenglong 2018/05/08 12:05
 *
 */
public class StaffPushDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 员工ID
	 */
	private int staffId;

	/**
	 * 员工姓名
	 */
	private String staffName;

	/**
	 * 所属组ID
	 */
	private String groupId;

	/**
	 * 所属组名称
	 */
	private String groupName;

	/**
	 * 今日领取客资数
	 */
	private int todayNum;

	/**
	 * 权重
	 */
	private int weight;

	/**
	 * 差比
	 */
	private double diffPid = Double.MAX_VALUE;

	/**
	 * 企业ID
	 */
	private int companyId;

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
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

	public double getDiffPid() {
		return diffPid;
	}

	public void setDiffPid(double diffPid) {
		this.diffPid = diffPid;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}