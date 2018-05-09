package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;

/**
 * 登录用户业务对象
 */
public class LoginUserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotEmptyStr(message = "{loginUser.phone.null}")
	private String userName;

	/**
	 * 密码
	 */
	@NotEmptyStr(message = "{loginUser.password.null}")
	private String password;

	/**
	 * 企业ID
	 */
	private int companyId;

	/**
	 * 验证码
	 */
	private String verifyCode;
	/**
	 * 是否是客户端
	 */
	private boolean clientFlag;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public boolean getClientFlag() {
		return clientFlag;
	}

	public void setClientFlag(boolean clientFlag) {
		this.clientFlag = clientFlag;
	}
}
