package com.qiein.jupiter.web.entity.dto;

/**
 * 验证token uid cid 数据传输对象
 */
public class VerifyParamDTO {
	/**
	 * token
	 */
	private String token;

	/**
	 * uid
	 */
	private int uid;

	/**
	 * cid
	 */
	private int cid;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
