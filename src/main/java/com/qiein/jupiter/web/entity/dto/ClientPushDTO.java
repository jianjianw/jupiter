package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 客资推送时封装客资信息
 * 
 * @author JingChenglong 2018/05/08 10:39
 *
 */
public class ClientPushDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 推送时间间隔
	 */
	private int pushInterval;


	/**
	 * 企业ID
	 */
	private int companyId;

	/**
	 * 主键ID
	 */
	private int id;

	/**
	 * 客资ID
	 */
	private String kzId;

	/**
	 * 状态ID
	 */
	private int statusId;

	/**
	 * 客服ID
	 */
	private int appointorId;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPushInterval() {
		return pushInterval;
	}

	public void setPushInterval(int pushInterval) {
		this.pushInterval = pushInterval;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getKzId() {
		return kzId;
	}

	public void setKzId(String kzId) {
		this.kzId = kzId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getAppointorId() {
		return appointorId;
	}

	public void setAppointorId(int appointorId) {
		this.appointorId = appointorId;
	}
}