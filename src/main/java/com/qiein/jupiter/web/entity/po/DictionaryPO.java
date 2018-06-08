package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

public class DictionaryPO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    private String dicType;
    /**
     * 编码
     */
    private int dicCode;
    /**
     * 名称
     */
    @NotEmptyStr(message = "{invalid.reason.name.null}")
    private String dicName;

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
    /**
     * 是否启用
     */
    private boolean showFlag;

    public DictionaryPO() {
    }

    public DictionaryPO(String dicType, int dicCode, String dicName, int companyId) {
        this.dicType = dicType;
        this.dicCode = dicCode;
        this.dicName = dicName;
        this.companyId = companyId;
    }

    public DictionaryPO(String dicType, int dicCode, String dicName, int priority, int companyId) {
        this.dicType = dicType;
        this.dicCode = dicCode;
        this.dicName = dicName;
        this.priority = priority;
        this.companyId = companyId;
    }

    public DictionaryPO(String dicType, String dicName, int companyId, int id) {
        super(id);
        this.dicType = dicType;
        this.dicName = dicName;
        this.companyId = companyId;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public int getDicCode() {
        return dicCode;
    }

    public void setDicCode(int dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }
}
