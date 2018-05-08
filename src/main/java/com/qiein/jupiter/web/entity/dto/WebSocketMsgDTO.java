package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.enums.WebSocketMsgEnum;

import java.io.Serializable;

/**
 * 公司全局消息
 */
public class WebSocketMsgDTO implements Serializable {

	private static final long serialVersionUID = -5853387409903101083L;
	/**
	 * 类型
	 */
	private WebSocketMsgEnum type;
	/**
	 * 公司ID
	 */
	private int companyId;
	/**
	 * 内容
	 */
	private String content;

	public WebSocketMsgEnum getType() {
		return type;
	}

	public void setType(WebSocketMsgEnum type) {
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
