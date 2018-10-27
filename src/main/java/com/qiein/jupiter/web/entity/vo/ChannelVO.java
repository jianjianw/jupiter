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
     * 渠道类型
     */
    private int typeId;
    /**
     * 来源集合
     */
    private LinkedList<SrcListVO> srcList;

    private int companyId;// 企业ID
    private String channelImg;// 图标
    private NumVO numVO;//每行数据


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getChannelImg() {
        return channelImg;
    }

    public void setChannelImg(String channelImg) {
        this.channelImg = channelImg;
    }

    public NumVO getNumVO() {
        return numVO;
    }

    public void setNumVO(NumVO numVO) {
        this.numVO = numVO;
    }
}
