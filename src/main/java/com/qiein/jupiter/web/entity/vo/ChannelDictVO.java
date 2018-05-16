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
    private String channleName;
    /**
     * 渠道图片地址
     */
    private String channleImg;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannleName() {
        return channleName;
    }

    public void setChannleName(String channleName) {
        this.channleName = channleName;
    }

    public String getChannleImg() {
        return channleImg;
    }

    public void setChannleImg(String channleImg) {
        this.channleImg = channleImg;
    }
}
