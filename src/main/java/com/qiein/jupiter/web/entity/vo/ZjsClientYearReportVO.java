package com.qiein.jupiter.web.entity.vo;


import com.qiein.jupiter.web.entity.dto.SourceClientDataDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库取出的年度转介绍客资数据
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 18:33
 */
public class ZjsClientYearReportVO {
    private String monthName;
    private String dataType;
    private List<SourceClientDataDTO> sourceData  = new ArrayList<>();  //来源数据

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<SourceClientDataDTO> getSourceData() {
        return sourceData;
    }

    public void setSourceData(List<SourceClientDataDTO> sourceData) {
        this.sourceData = sourceData;
    }

    @Override
    public String toString() {
        return "ZjsClientYearReportVO{" +
                "monthName='" + monthName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", sourceData=" + sourceData +
                '}';
    }
}
