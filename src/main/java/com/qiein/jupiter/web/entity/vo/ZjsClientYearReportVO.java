package com.qiein.jupiter.web.entity.vo;

import java.util.Map;

/**
 * 从数据库取出的年度转介绍客资数据
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 18:33
 */
public class ZjsClientYearReportVO {
    private String monthName;
    private Map<Integer,Map<String,Object>> dataMap;

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Map<Integer, Map<String, Object>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Integer, Map<String, Object>> dataMap) {
        this.dataMap = dataMap;
    }
}
