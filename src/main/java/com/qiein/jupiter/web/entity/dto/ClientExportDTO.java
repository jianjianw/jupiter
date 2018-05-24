package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 客资导出参数封装类
 * 2018/5/24
 * gaoxiaoli
 */
public class ClientExportDTO implements Serializable {
    private static final long serialVersionUID = -8710248965916764304L;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 员工ID
     */
    private String uid;
    /**
     * token
     */
    private String sig;
    /**
     * 接口名称
     */
    private String action;
    /**
     * 角色
     */
    private String role;
    /**
     * 搜索客资时间类型
     */
    private String timeType;
    /**
     * 开始时间
     */
    private String start;
    /**
     * 结束时间
     */
    private String end;
    /**
     * 渠道ID
     */
    private String channelId;
    /**
     * 来源ID
     */
    private String sourceId;
    /**
     * 拍摄地ID
     */
    private String shopId;
    /**
     * 搜索员工的ID
     */
    private String staffId;
    /**
     * 婚纱照类型
     */
    private String typeId;
    /**
     * 意向等级
     */
    private String yxLevel;
    /**
     * 邀约客服ID拼接
     */
    private String appointIds;
    private String pmsLimit;
    private String linkLimit;
    private String spareSql;
    /**
     * 过滤sql拼接
     */
    private String filterSql;
    /**
     * 高级搜索sql拼接
     */
    private String superSql;


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(String yxLevel) {
        this.yxLevel = yxLevel;
    }

    public String getAppointIds() {
        return appointIds;
    }

    public void setAppointIds(String appointIds) {
        this.appointIds = appointIds;
    }

    public String getPmsLimit() {
        return pmsLimit;
    }

    public void setPmsLimit(String pmsLimit) {
        this.pmsLimit = pmsLimit;
    }

    public String getLinkLimit() {
        return linkLimit;
    }

    public void setLinkLimit(String linkLimit) {
        this.linkLimit = linkLimit;
    }

    public String getSpareSql() {
        return spareSql;
    }

    public void setSpareSql(String spareSql) {
        this.spareSql = spareSql;
    }

    public String getFilterSql() {
        return filterSql;
    }

    public void setFilterSql(String filterSql) {
        this.filterSql = filterSql;
    }

    public String getSuperSql() {
        return superSql;
    }

    public void setSuperSql(String superSql) {
        this.superSql = superSql;
    }
}
