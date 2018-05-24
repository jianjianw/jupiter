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
    private int uid;
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
    private int timeType;
    /**
     * 开始时间
     */
    private int start;
    /**
     * 结束时间
     */
    private int end;
    /**
     * 渠道ID
     */
    private int channelId;
    /**
     * 来源ID
     */
    private int sourceId;
    /**
     * 拍摄地ID
     */
    private int shopId;
    /**
     * 搜索员工的ID
     */
    private int staffId;
    /**
     * 婚纱照类型
     */
    private int typeId;
    /**
     * 意向等级
     */
    private int yxLevel;
    /**
     * 邀约客服ID拼接
     */
    private String appointIds;
    private int pmsLimit;
    private int linkLimit;
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(int yxLevel) {
        this.yxLevel = yxLevel;
    }

    public String getAppointIds() {
        return appointIds;
    }

    public void setAppointIds(String appointIds) {
        this.appointIds = appointIds;
    }

    public int getPmsLimit() {
        return pmsLimit;
    }

    public void setPmsLimit(int pmsLimit) {
        this.pmsLimit = pmsLimit;
    }

    public int getLinkLimit() {
        return linkLimit;
    }

    public void setLinkLimit(int linkLimit) {
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
