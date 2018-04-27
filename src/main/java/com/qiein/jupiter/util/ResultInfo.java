package com.qiein.jupiter.util;

import java.io.Serializable;

/**
 * 返回结果封装
 */
public class ResultInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;

	private String msg;

	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}