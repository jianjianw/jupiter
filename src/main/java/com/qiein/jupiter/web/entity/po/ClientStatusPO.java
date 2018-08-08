package com.qiein.jupiter.web.entity.po;

/**
 * FileName: ClientStatusPO
 *
 * @author: yyx
 * @Date: 2018-6-20 14:36
 */
public class ClientStatusPO {
    /**
     * id
     * */
    private Integer id;
    /**
     * classid
     * */
    private Integer classId;
    /**
     * className
     * */
    private String className;
    /**
     * statusid
     * */
    private Integer statusId;
    /**
     * statusName
     * */
    private String statusName;
    /**
     * 背景颜色
     * */
    private String backColor;
    /**
     * 字体颜色
     * */
    private String fontColor;
    /**
     * companyId
     * */
    private Integer companyId;
    /**
     * 客资状态有效性
     */
    private int showFlag;

    public int getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(int showFlag) {
        this.showFlag = showFlag;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
