package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 报表统计接收参数
 *
 * @author gaoxiaoli 2018/7/5
 */

public class AnalyzeVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private int companyId;
    /**
     * 开始时间
     */
    private int start;
    /**
     * 结束时间
     */
    private int end;
    /**
     * 拍照类型
     */
    private String photoTypes;
    /**
     * 来源ID
     */
    private String sourceIds;
    /**
     * 咨询类型
     */
    private String zxStyle;
    /**
     * 员工ID
     *
     * @return
     */
    private String staffId;

    /**
     * 小组ID
     *
     * @return
     */
    private String groupId;

    /**
     * 门店
     */
    private String shopIds;
    /**
     * 渠道ID
     */
    private String channelIds;
    /**
     * 纯电商推广筛选
     */
    private int typeLimt;

    private List<Integer> staffList;

    private int sourceId;


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getPhotoTypes() {
        return photoTypes;
    }

    public void setPhotoTypes(String photoTypes) {
        this.photoTypes = photoTypes;
    }

    public String getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(String sourceIds) {
        this.sourceIds = sourceIds;
    }

    public String getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(String zxStyle) {
        this.zxStyle = zxStyle;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getShopIds() {
        return shopIds;
    }

    public void setShopIds(String shopIds) {
        this.shopIds = shopIds;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public int getTypeLimt() {
        return typeLimt;
    }

    public void setTypeLimt(int typeLimt) {
        this.typeLimt = typeLimt;
    }

    public List<Integer> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Integer> staffList) {
        this.staffList = staffList;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}
