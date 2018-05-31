package com.qiein.jupiter.web.entity.vo;

/**
 * 怠工日志列表
 * Created by Tt on 2018/5/16 0016.
 */
public class LazyWorkVO{
    private Long allotTime;
    private Integer staffId;
    private String staffName;
    private Integer kzNum;
    private Integer statusId;
    private String kzId;
    private Integer channelId;
    private Integer sourceId;
    private Integer companyId;
    /**
     * 查询时多选的staffid
     */
    private String staffIds;
    /**
     * 查询的开始时间
     */
    private Integer startTime;
    /**
     * 查询的结束时间
     */
    private Integer endTime;

    public String getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(String staffIds) {
        this.staffIds = staffIds;
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

    public Long getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(Long allotTime) {
        this.allotTime = allotTime;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getKzNum() {
        return kzNum;
    }

    public void setKzNum(Integer kzNum) {
        this.kzNum = kzNum;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
