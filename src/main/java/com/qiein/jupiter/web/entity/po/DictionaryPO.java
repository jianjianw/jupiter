package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

import java.io.Serializable;

public class DictionaryPO extends BaseEntity {
    /**
     * 类型
     */
    private String dicType;
    /**
     * 编码
     */
    private String dicCode;
    /**
     * 名称
     */
    private String dicName;
    /**
     * 备注
     */
    private String memo;
    /**
     * 顺序
     */
    private int priority;
    /**
     * 备用字段
     */
    private String spare;
    /**
     * 企业ID
     */
    private int companyId;

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSpare() {
        return spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

}
