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
    private Map<String,Map<String,Integer>> dataMap;

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

    public Map<String, Map<String, Integer>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Map<String, Integer>> dataMap) {
        this.dataMap = dataMap;
    }
}
