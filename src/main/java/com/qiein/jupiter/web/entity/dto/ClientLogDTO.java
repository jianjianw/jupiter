package com.qiein.jupiter.web.entity.dto;

public class ClientLogDTO {
    private Integer statusId;//状态id
    private Integer channelId;//渠道id
    private Integer typeId;//联系方式id
    private String value;//联系方式
    private Integer startTime;//开始时间
    private Integer endTime;//结束时间
    private Integer companyId;
    private String tableInfo;
    private String tableLog;
    private String tableEditLog;
    private String tableDetail;

    public String getTableDetail() {
        return tableDetail;
    }

    public void setTableDetail(String tableDetail) {
        this.tableDetail = tableDetail;
    }

    public String getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getTableLog() {
        return tableLog;
    }

    public void setTableLog(String tableLog) {
        this.tableLog = tableLog;
    }

    public String getTableEditLog() {
        return tableEditLog;
    }

    public void setTableEditLog(String tableEditLog) {
        this.tableEditLog = tableEditLog;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}