package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * goeasy推送时封装客资信息
 *
 * @author gaoxiaoli 2018/05/25 10:39
 */
public class ClientGoEasyDTO implements Serializable {

	private static final long serialVersionUID = 1L;
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
	 * 客资姓名
	 */
	private String kzName;
	/**
	 * 客资手机号
	 */
	private String kzPhone;
	/**
	 * 客资微信
	 */
	private String kzWechat;
	/**
	 * 客资QQ
	 */
	private String kzQq;
	/**
	 * 客资旺旺
	 */
	private String kzWw;
	/**
	 * 渠道名称
	 */
	private String channelName;
	/**
	 * 来源名称
	 */
	private String sourceName;
	/**
	 * 无效原因
	 */
	private String invalidLabel;
	/**
	 * 推广人ID
	 */
	private int collectorId;
	/**
	 * 订单时间
	 */
	private int successTime;
	/**
	 * 成交套系金额
	 */
	private int amount;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 录入时间
	 */
	private int createTime;
	/**
	 * 录入人姓名
	 */
	private String collectorName;

	public String getKzQq() {
		return kzQq;
	}

	public void setKzQq(String kzQq) {
		this.kzQq = kzQq;
	}

	public String getKzWw() {
		return kzWw;
	}

	public void setKzWw(String kzWw) {
		this.kzWw = kzWw;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKzId() {
		return kzId;
	}

	public void setKzId(String kzId) {
		this.kzId = kzId;
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

	public String getKzName() {
		return kzName;
	}

	public void setKzName(String kzName) {
		this.kzName = kzName;
	}

	public String getKzPhone() {
		return kzPhone;
	}

	public void setKzPhone(String kzPhone) {
		this.kzPhone = kzPhone;
	}

	public String getKzWechat() {
		return kzWechat;
	}

	public void setKzWechat(String kzWechat) {
		this.kzWechat = kzWechat;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getInvalidLabel() {
		return invalidLabel;
	}

	public void setInvalidLabel(String invalidLabel) {
		this.invalidLabel = invalidLabel;
	}

	public int getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(int collectorId) {
		this.collectorId = collectorId;
	}

	public int getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(int successTime) {
		this.successTime = successTime;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}
}