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
	private double diffPid;

	/**
	 * 企业ID
	 */
	private int companyId;

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