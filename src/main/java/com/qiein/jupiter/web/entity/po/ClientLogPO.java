package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

import com.qiein.jupiter.constant.CommonConstant;

/**
 * 客资日志
 * 
 * @author JingChenglong 2018/05/09 17:37
 *
 */
public class ClientLogPO implements Serializable {

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
	 * 操作者ID
	 */
	private int operaId;

	/**
	 * 操作者姓名
	 */
	private String operaName = "";

	/**
	 * 操作内容
	 */
	private String memo;

	/**
	 * 日志类型
	 */
	private int logType;

	/**
	 * 企业ID
	 */
	private int companyId;

	public ClientLogPO(String kzId, int operaId, String operaName, String memo, int logType, int companyId) {
		super();
		this.kzId = kzId;
		this.operaId = operaId;
		this.operaName = operaName;
		this.memo = memo;
		this.logType = logType;
		this.companyId = companyId;
	}

	public ClientLogPO(String kzId, String memo, int logType, int companyId) {
		super();
		this.kzId = kzId;
		this.operaName = CommonConstant.SYSTEM_OPERA_NAME;
		this.memo = memo;
		this.logType = logType;
		this.companyId = companyId;
	}

	public ClientLogPO() {

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

	public int getOperaId() {
		return operaId;
	}

	public void setOperaId(int operaId) {
		this.operaId = operaId;
	}

	public String getOperaName() {
		return operaName;
	}

	public void setOperaName(String operaName) {
		this.operaName = operaName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}