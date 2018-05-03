package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 客资状态
 */
public class StatusPO extends BaseEntity {

    /**
     * 分类ID
     */
    private int classId;
    /**
     * 分类名称
     */
    private String className;
    /**
     * 状态ID
     */
    private int statusId;
    /**
     * 状态名
     */
    private String statusName;
    /**
     * 背景色
     */
    private String backColor;
    /**
     * 字体色
     */
    private String fontColor;
    /**
     * 企业ID
     */
    private int companyId;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
