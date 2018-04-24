package com.qiein.jupiter.web.entity.vo;

/**
 * 渠道下的来源集合
 */
public class SrcListVO {
    /**
     * 来源ID
     */
    private int srcId;
    /**
     * 来源名称
     */
    private String srcName;

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
}
