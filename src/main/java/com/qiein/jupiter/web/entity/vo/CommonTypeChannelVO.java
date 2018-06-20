package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CommonTypePO;

import java.util.List;

/**
 * author xiangliang
 */
public class CommonTypeChannelVO {
    //来源
    private Integer channelId;
    private String channelImg;
    private String channelName;
    //渠道
    private List<CommonTypePO> list;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelImg() {
        return channelImg;
    }

    public void setChannelImg(String channelImg) {
        this.channelImg = channelImg;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<CommonTypePO> getList() {
        return list;
    }

    public void setList(List<CommonTypePO> list) {
        this.list = list;
    }
}
