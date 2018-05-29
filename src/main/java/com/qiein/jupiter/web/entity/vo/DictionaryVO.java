package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 新增咨询类型字典VO Created by Tt on 2018/5/23 0023.
 */
public class DictionaryVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String dicCodes;
	private int companyId;

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getDicCodes() {
		return dicCodes;
	}

	public void setDicCodes(String dicCodes) {
		this.dicCodes = dicCodes;
	}
}
