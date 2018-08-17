package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 17:26
 */
public class ZjsClientYearReportDTO {
    private int year;
    private String sourceIds;
    private String dataType;    //数据类型  客资量 有效量 入店量 成交量
    private int companyId;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(String sourceIds) {
        this.sourceIds = sourceIds;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "ZjsClientYearReportDTO{" +
                "year=" + year +
                ", sourceIds='" + sourceIds + '\'' +
                ", dataType='" + dataType + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
