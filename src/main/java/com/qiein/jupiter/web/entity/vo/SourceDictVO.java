package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 渠道的页面字典
 *
 * @Author: shiTao
 */
public class SourceDictVO implements Serializable {

    private static final long serialVersionUID = 6039167760834104308L;

    private int id;
    /**
     * 来源名称
     */
    private String srcName;
    /**
     * 来源图片地址
     */
    private String srcImg;
    /**
     * 是否显示
     */
    private boolean showFlag;

    /**
     * 分配规则
     */
    private int pushRule;


    public int getPushRule() {
        return pushRule;
    }

    public void setPushRule(int pushRule) {
        this.pushRule = pushRule;
    }

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

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }
}
