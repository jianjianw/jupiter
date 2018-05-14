package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 系统日志
 * 
 * @author JingChenglong 2018/05/14 14:36
 *
 */
public class SystemLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private int id;

	/**
	 * 业务类型ID
	 */
	private int typeId;

	/**
	 * 请求点IP
	 */
	private String ip;

	/**
	 * IP归属
	 */
	private String address;

	/**
	 * 请求路径
	 */
	private String url;

	/**
	 * 人员ID
	 */
	private int staffId;

	/**
	 * 人员名称
	 */
	private String staffName;

	/**
	 * 操作描述
	 */
	private String memo;

	/**
	 * 操作时间
	 */
	private int logTime;

	/**
	 * mac地址
	 */
	private String mac;

	/**
	 * 企业ID
	 */
	private int companyId;

	public SystemLog(int typeId, String ip, String url, int staffId, String staffName, String memo, int companyId) {
		super();
		this.typeId = typeId;
		this.ip = ip;
		this.url = url;
		this.staffId = staffId;
		this.staffName = staffName;
		this.memo = memo;
		this.companyId = companyId;
	}

	public SystemLog() {
		super();
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getLogTime() {
		return logTime;
	}

	public void setLogTime(int logTime) {
		this.logTime = logTime;
	}
}