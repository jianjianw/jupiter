package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 拍摄地渠道小组关联
 */
public class ShopChannelGroupVO implements Serializable {
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
	 * 渠道图标
	 */
	private String channelImg;

	/**
	 * 渠道小组关联集合
	 */
	private List<ChannelGroupVO> groupList;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelImg() {
		return channelImg;
	}

	public void setChannelImg(String channelImg) {
		this.channelImg = channelImg;
	}

	public List<ChannelGroupVO> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<ChannelGroupVO> groupList) {
		this.groupList = groupList;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
}
