package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 转介绍提报统计
 *
 * @author gaoxiaoli 2018/7/5
 */

public class ZjsSourceVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int sourceId;// 来源ID
    private String sourceName;// 来源名称
    private int companyId;// 企业ID
    private String sourceImg;// 图标
    private String groupId;//小组ID
    private String groupName;//小组名称
    private int staffId;//员工ID
    private String nickName;//员工名称
    private ZjsNumVO zjsNumVO;//每行数据

    public ZjsSourceVO() {
    }

    public ZjsSourceVO(String type, int companyId) {
        if ("total".equalsIgnoreCase(type)) {
            this.groupId = "0";
            this.groupName = "合计";
            this.sourceId = 0;
            this.sourceName = "合计";
            this.sourceImg = "";
            this.companyId = companyId;
            this.staffId = -1;
            this.nickName = "合计";
            this.zjsNumVO = new ZjsNumVO(type);
        }
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getSourceImg() {
        return sourceImg;
    }

    public void setSourceImg(String sourceImg) {
        this.sourceImg = sourceImg;
    }

    public ZjsNumVO getZjsNumVO() {
        return zjsNumVO;
    }

    public void setZjsNumVO(ZjsNumVO zjsNumVO) {
        this.zjsNumVO = zjsNumVO;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
