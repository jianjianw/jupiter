package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 渠道页面字典
 *
 * @Author: shiTao
 */
public class ChannelDictVO implements Serializable {

    private static final long serialVersionUID = -8866800767401130L;

    private int id;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道图片地址
     */
    private String channelImg;
    /**
     * 是否显示
     */
    private boolean showFlag;

    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
