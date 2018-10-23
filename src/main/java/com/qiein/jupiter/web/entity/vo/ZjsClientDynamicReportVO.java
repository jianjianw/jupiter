package com.qiein.jupiter.web.entity.vo;


import java.io.Serializable;
import java.util.List;

public class ZjsClientDynamicReportVO implements Serializable {

    private static final long serialVersionUID = 8580261613557433027L;

    private List<String> dynamicTableHead;

    private List<Object> dynamicData;


    public List<String> getDynamicTableHead() {
        return dynamicTableHead;
    }

    public void setDynamicTableHead(List<String> dynamicTableHead) {
        this.dynamicTableHead = dynamicTableHead;
    }

    public List<Object> getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(List<Object> dynamicData) {
        this.dynamicData = dynamicData;
    }
}
