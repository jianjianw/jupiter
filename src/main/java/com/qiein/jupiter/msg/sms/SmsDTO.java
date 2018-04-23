package com.qiein.jupiter.msg.sms;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.StringUtil;

/**
 * 短信发送基础数据
 * 
 * @author JingChenglong 2018/04/19 11:33
 *
 */
public class SmsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信签名
	 */
	private String sign;

	/**
	 * 短信模板
	 */
	private String templateId;

	/**
	 * 短信内容
	 */
	private JSONObject content;

	public SmsDTO() {
		super();
	}

	public SmsDTO(String sign, String templateId) {
		super();
		this.sign = sign;
		this.templateId = templateId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}

	public boolean isNotEmpty() {
		return (StringUtil.isEmpty(this.sign) && StringUtil.isEmpty(this.templateId) && this.content != null);
	}
}