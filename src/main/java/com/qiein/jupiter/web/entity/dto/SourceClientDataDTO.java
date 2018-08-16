package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/15 21:13
 */
public class SourceClientDataDTO {
    private int srcId;              //渠道id
    private String srcName;         //渠道名
    private String srcImg;          //渠道图标
    private int dataNum;            //数据值

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

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

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    @Override
    public String toString() {
        return "SourceClientData{" +
                "srcId=" + srcId +
                ", srcName='" + srcName + '\'' +
                ", srcImg='" + srcImg + '\'' +
                ", dataNum=" + dataNum +
                '}';
    }
}
