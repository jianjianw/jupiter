package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 客资分配日志
 * 
 * @author JingChenglong 2018/05/09 11:27
 *
 */
public class AllotLogPO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志ID
	 */
	private int id;

	/**
	 * 客资ID
	 */
	private String kzId;

	/**
	 * 分配时间
	 */
	private int allotTime;

	/**
	 * 员工ID
	 */
	private int staffId;

	/**
	 * 员工姓名
	 */
	private String staffName;

	/**
	 * 客服组ID
	 */
	private String groupId;

	/**
	 * 客服组名称
	 */
	private String groupName;

	/**
	 * 领取状态ID
	 */
	private int statusId;

	/**
	 * 领取时间
	 */
	private int receiveTime;

	/**
	 * 分配类型
	 */
	private int allotType;

	/**
	 * 企业ID
	 */
	private int companyId;

	public AllotLogPO(String kzId, int staffId, String staffName, String groupId, String groupName, int statusId,
			int allotType, int companyId) {
		super();
		this.kzId = kzId;
		this.staffId = staffId;
		this.staffName = staffName;
		this.groupId = groupId;
		this.groupName = groupName;
		this.statusId = statusId;
		this.allotType = allotType;
		this.companyId = companyId;
	}

	public AllotLogPO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKzId() {
		return kzId;
	}

	public void setKzId(String kzId) {
		this.kzId = kzId;
	}

	public int getAllotTime() {
		return allotTime;
	}

	public void setAllotTime(int allotTime) {
		this.allotTime = allotTime;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(int receiveTime) {
		this.receiveTime = receiveTime;
	}

	public int getAllotType() {
		return allotType;
	}

	public void setAllotType(int allotType) {
		this.allotType = allotType;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}