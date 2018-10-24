package com.qiein.jupiter.web.entity.vo;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ZjsClientDynamicReportVO implements Serializable {

    private static final long serialVersionUID = 8580261613557433027L;

    private Map<String,String> dynamicTableHead;

    private List<Object> dynamicData;


    public Map<String, String> getDynamicTableHead() {
        return dynamicTableHead;
    }

    public void setDynamicTableHead(Map<String, String> dynamicTableHead) {
        this.dynamicTableHead = dynamicTableHead;
    }

    public List<Object> getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(List<Object> dynamicData) {
        this.dynamicData = dynamicData;
    }
}
