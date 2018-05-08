package com.qiein.jupiter.web.entity.dto;

import java.util.Map;

public class QueryMapDTO {
	/**
	 * 当前页
	 */
	private int pageNum;

	/**
	 * 每次查询多少
	 */
	private int pageSize;

	/**
	 * 条件
	 */
	private Map condition;

	public int getPageNum() {
		// 设置默认值
		if (pageNum == 0)
			pageNum = 1;
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		// 设置默认值
		if (pageSize == 0)
			pageSize = 30;
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map getCondition() {
		return condition;
	}

	public void setCondition(Map condition) {
		this.condition = condition;
	}
}
