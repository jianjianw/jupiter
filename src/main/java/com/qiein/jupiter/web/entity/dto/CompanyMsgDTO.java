package com.qiein.jupiter.web.entity.dto;

/**
 * 公司全局消息
 */
public class CompanyMsgDTO {
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 公司ID
	 */
	private int companyId;
	/**
	 * 内容
	 */
	private String content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
