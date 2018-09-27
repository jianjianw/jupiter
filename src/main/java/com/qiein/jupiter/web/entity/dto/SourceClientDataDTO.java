package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/15 21:13
 */
public class SourceClientDataDTO {
    private int srcId;              //渠道id
    private String srcName;         //渠道名
    private String srcImg;          //渠道图标
    private String dataNum;            //数据值
    private String provinceName;    //省份

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

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "SourceClientDataDTO{" +
                "srcId=" + srcId +
                ", srcName='" + srcName + '\'' +
                ", srcImg='" + srcImg + '\'' +
                ", dataNum='" + dataNum + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
