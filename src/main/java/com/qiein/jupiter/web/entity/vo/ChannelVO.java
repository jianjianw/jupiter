package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 渠道来源集合
 */
public class ChannelVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 渠道ID
	 */
	private int channelId;
	/**
	 * 渠道名称
	 */
	private String channelName;
	/**
	 * 来源集合
	 */
	private LinkedList<SrcListVO> srcList;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public LinkedList<SrcListVO> getSrcList() {
		return srcList;
	}

	public void setSrcList(LinkedList<SrcListVO> srcList) {
		this.srcList = srcList;
	}
}
