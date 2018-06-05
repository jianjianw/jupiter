package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.DictionaryPO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新增咨询类型字典VO
 * Created by Tt(叶华葳)
 * on 2018/5/23 0023.
 */
public class DictionaryVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String dicCodes;
	private String dicNames;
	private List<DictionaryPO> Diclist = new ArrayList<>();
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

	public String getDicNames() {
		return dicNames;
	}

	public void setDicNames(String dicNames) {
		this.dicNames = dicNames;
	}

	public List<DictionaryPO> getDiclist() {
		return Diclist;
	}

	public void setDiclist(List<DictionaryPO> diclist) {
		Diclist = diclist;
	}
}
