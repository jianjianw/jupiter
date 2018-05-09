package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 消息类型的总数量及是否存在未读类型
 */
public class NewsTotalAmountAndFlag implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 所有的消息数量
	 */
	private long allAmount;
	/**
	 * 是否存在客资类型的未读
	 */
	private boolean kzType;
	/**
	 * 是否存在定时类型的未读
	 */
	private boolean noticeType;
	/**
	 * 是否存在系统类型的未读
	 */
	private boolean systemType;

	public long getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(long allAmount) {
		this.allAmount = allAmount;
	}

	public boolean isKzType() {
		return kzType;
	}

	public void setKzType(boolean kzType) {
		this.kzType = kzType;
	}

	public boolean isNoticeType() {
		return noticeType;
	}

	public void setNoticeType(boolean noticeType) {
		this.noticeType = noticeType;
	}

	public boolean isSystemType() {
		return systemType;
	}

	public void setSystemType(boolean systemType) {
		this.systemType = systemType;
	}
}
