package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 页面设置
 */
public class PageConfig implements Serializable {
    private static final long serialVersionUID = -3744290554665818652L;

    private int id;
    /**
     * 角色
     */
    private String role;
    /**
     * dscj_new
     */
    private String action;
    /**
     * 描述
     */
    private String memo;
    /**
     * 表头
     */
    private String titleTxt;

    /**
     * 表头-不展示的
     */
    private String titleTxtNo;
    /**
     * 排序
     */
    private int priority;
    /**
     * 是否显示
     */
    private boolean showFlag;
    /**
     * action 对应的sql
     */
    private String actionSql;
    /**
     * SQL条件的序列化
     */
    private String sqlCondition;
    /**
     * 公司ID
     */
    private int companyId;


    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }

    public String getActionSql() {
        return actionSql;
    }

    public void setActionSql(String actionSql) {
        this.actionSql = actionSql;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }


    public String getTitleTxtNo() {
        return titleTxtNo;
    }

    public void setTitleTxtNo(String titleTxtNo) {
        this.titleTxtNo = titleTxtNo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
