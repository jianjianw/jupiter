package com.qiein.jupiter.web.entity.vo;

/**
 * 怠工日志列表
 * Created by Tt on 2018/5/16 0016.
 */
public class LazyWorkVO{
    private Long allotTime;
    private Integer staffId;
    private String staffName;
    private String staffPhone;
    private String headImg;
    private Integer kzNum;
    private Integer statusId;
    private String kzId;
    private Integer channelId;
    private Integer sourceId;
    private Integer groupId;
    private String groupName;
    private Integer companyId;
    /**
     * 查询时多选的sourceId
     */
    private String sourceIds;
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
    /**
     * 当前页数
     */
    private Integer pageNum = 1;
    /**
     * 当前页码
     */
    private Integer pageSize = 50;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(String sourceIds) {
        this.sourceIds = sourceIds;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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
