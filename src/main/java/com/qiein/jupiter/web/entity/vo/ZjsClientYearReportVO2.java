package com.qiein.jupiter.web.entity.vo;

import java.util.Map;

/**
 * 返回给前端的转介绍年度客资数据
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 19:00
 */
public class ZjsClientYearReportVO2 {
    private int srcId;
    private String srcName;
    private String srcImg;
    private String dataType;    //数据类型 总客资 客资量 有效量 入店量 成交量
    private Map<String,String> dataMap;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public String toString() {
        return "ZjsClientYearReportVO2{" +
                "srcId=" + srcId +
                ", srcName='" + srcName + '\'' +
                ", srcImg='" + srcImg + '\'' +
                ", dataType='" + dataType + '\'' +
                ", dataMap=" + dataMap +
                '}';
    }
}
