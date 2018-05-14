package com.qiein.jupiter.web.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 实体类基类
 *
 * @author JZL 2018-04-03 17:25
 */
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String url;

	private String params;

	private String ip;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BaseEntity() {
	}

	public BaseEntity(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}