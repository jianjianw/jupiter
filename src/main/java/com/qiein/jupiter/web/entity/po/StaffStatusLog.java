package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 员工状态日志
 *
 * @author shiTao
 */
public class StaffStatusLog implements Serializable {
	private static final long serialVersionUID = 7222442536607896438L;

	private int id;
	/**
	 * 员工Id
	 */
	private int staffId;
	/**
	 * 状态编码0-下线，1-上线，8-停单，9-满限
	 */
	private int statusCode;
	/**
	 * 操作人Id
	 */
	private Integer operaId;
	/**
	 * 操作人姓名
	 */
	private String operaName;
	/**
	 * 时间
	 */
	private int logTime;
	/**
	 * 公司Id
	 */
	private int companyId;

	private String memo;

	public StaffStatusLog() {
	}

	public StaffStatusLog(int staffId, int statusCode, Integer operaId, String operaName, int companyId, String memo) {
		this.staffId = staffId;
		this.statusCode = statusCode;
		this.operaId = operaId;
		this.operaName = operaName;
		this.companyId = companyId;
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getOperaId() {
		return operaId;
	}

	public void setOperaId(Integer operaId) {
		this.operaId = operaId;
	}

	public String getOperaName() {
		return operaName;
	}

	public void setOperaName(String operaName) {
		this.operaName = operaName;
	}

	public int getLogTime() {
		return logTime;
	}

	public void setLogTime(int logTime) {
		this.logTime = logTime;
	}
}
