package com.qiein.jupiter.msg.goeasy;

import com.qiein.jupiter.util.StringUtil;

import java.io.Serializable;


/**
 * 客资信息简单传输
 * 
 * @author JingChenglong 2018/04/17 17:01
 *
 */
public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 客资ID
	 */
	private String kzId;

	/**
	 * 客资名
	 */
	private String kzName;

	/**
	 * 客资手机号
	 */
	private String kzPhone;

	/**
	 * 客资微信号
	 */
	private String kzWeChat;

	/**
	 * 客资QQ
	 */
	private String kzQq;

	/**
	 * 录入人名字
	 */
	private String collector;

	/**
	 * 客服名字
	 */
	private String appointor;

	/**
	 * 渠道名
	 */
	private String channelName;

	/**
	 * 来源名
	 */
	private String srcName;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 录入时间
	 */
	private String createTime;

	public String getKzId() {
		return kzId;
	}

	public void setKzId(String kzId) {
		this.kzId = kzId;
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

	public String getKzWeChat() {
		return kzWeChat;
	}

	public void setKzWeChat(String kzWeChat) {
		this.kzWeChat = kzWeChat;
	}

	public String getKzQq() {
		return kzQq;
	}

	public void setKzQq(String kzQq) {
		this.kzQq = kzQq;
	}

	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getAppointor() {
		return appointor;
	}

	public void setAppointor(String appointor) {
		this.appointor = appointor;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取客资的渠道和来源
	 * 
	 * @return
	 */
	public String getChannelSource() {
		if (StringUtil. isEmpty(this.channelName)) {
			if (StringUtil. isEmpty(this.srcName)) {
				return (this.channelName + " - " + this.srcName);
			} else {
				return this.channelName;
			}
		} else {
			if (StringUtil. isEmpty(this.srcName)) {
				return this.srcName;
			} else {
				return "";
			}
		}
	}
}