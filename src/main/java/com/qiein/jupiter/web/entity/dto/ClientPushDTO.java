package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;

/**
 * 客资推送时封装客资信息
 *
 * @author JingChenglong 2018/05/08 10:39
 */
public class ClientPushDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 推送时间间隔
	 */
	private int pushInterval;

	/**
	 * 客资领取超时时间
	 */
	private int overTime;

	/**
	 * 企业ID
	 */
	private int companyId;

	/**
	 * 主键ID
	 */
	private int id;

	/**
	 * 客资ID
	 */
	private String kzId;

	/**
	 * 状态ID
	 */
	private int statusId;

	/**
	 * 客服ID
	 */
	private int appointorId;
	/**
	 * 来源ID
	 */
	private int sourceId;
	/**
	 * 邀约名称
	 */
	private String appointName;

	/**
	 * 最终拍摄地名称
	 */
	private String filmingArea;

	/**
	 * 推送规则
	 */
	private int pushRule;

	/**
	 * 拍摄地ID
	 */
	private int shopId;

	/**
	 * 渠道ID
	 */
	private int channelId;

	/**
	 * 渠道类型
	 */
	private int channelTypeId;

	public ClientPushDTO(Integer pushRule, int companyId, String kzId, int shopId, int channelId, Integer channelTypeId,
			int overtime, int kzInterval) {
		this.pushRule = pushRule;
		this.companyId = companyId;
		this.kzId = kzId;
		this.shopId = shopId;
		this.channelId = channelId;
		this.channelTypeId = channelTypeId;
		this.overTime = overtime;
		this.pushInterval = kzInterval;
	}

	public ClientPushDTO() {
		super();
	}

	public boolean isNotEmpty() {
		return (NumUtil.isValid(this.pushRule) && NumUtil.isValid(this.companyId) && StringUtil.isValid(this.kzId)
				&& NumUtil.isValid(this.shopId) && NumUtil.isValid(this.channelId)
				&& NumUtil.isValid(this.channelTypeId));
	}

	public int getOverTime() {
		return overTime;
	}

	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}

	public int getPushRule() {
		return pushRule;
	}

	public void setPushRule(int pushRule) {
		this.pushRule = pushRule;
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

	public int getChannelTypeId() {
		return channelTypeId;
	}

	public void setChannelTypeId(int channelTypeId) {
		this.channelTypeId = channelTypeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPushInterval() {
		return pushInterval;
	}

	public void setPushInterval(int pushInterval) {
		this.pushInterval = pushInterval;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getKzId() {
		return kzId;
	}

	public void setKzId(String kzId) {
		this.kzId = kzId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getAppointorId() {
		return appointorId;
	}

	public void setAppointorId(int appointorId) {
		this.appointorId = appointorId;
	}

	public int getSourceId() {
		return sourceId;
	} 

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getAppointName() {
		return appointName;
	}

	public void setAppointName(String appointName) {
		this.appointName = appointName;
	}

	public String getFilmingArea() {
		return filmingArea;
	}

	public void setFilmingArea(String filmingArea) {
		this.filmingArea = filmingArea;
	}
}