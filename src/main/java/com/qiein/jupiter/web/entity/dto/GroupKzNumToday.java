package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 渠道客资领取数量
 * 
 * @author JingChenglong 2018/05/08 17:09
 *
 */
public class GroupKzNumToday implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 客服组ID
	 */
	private String groupId;

	/**
	 * 拍摄地ID
	 */
	private int shopId;

	/**
	 * 渠道ID
	 */
	private int channelId;

	/**
	 * 企业ID
	 */
	private int companyId;

	/**
	 * 客资量
	 */
	private int kzNum;

	public int getKzNum() {
		return kzNum;
	}

	public void setKzNum(int kzNum) {
		this.kzNum = kzNum;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}