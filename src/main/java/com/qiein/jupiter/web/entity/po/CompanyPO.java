package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 公司数据库对象
 *
 */
public class CompanyPO extends BaseEntity {

    private static final long serialVersionUID = -6524936685705376975L;

    private String compName;
    private int typeId;
    private String logo;
    private String corpId;
    private String config;
    private boolean ipLimit;
    private boolean ssoLimit;
    private int overtime;
    private int limitDefault;
    private int kzInterval;

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public boolean isIpLimit() {
        return ipLimit;
    }

    public void setIpLimit(boolean ipLimit) {
        this.ipLimit = ipLimit;
    }

    public boolean isSsoLimit() {
        return ssoLimit;
    }

    public void setSsoLimit(boolean ssoLimit) {
        this.ssoLimit = ssoLimit;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getLimitDefault() {
        return limitDefault;
    }

    public void setLimitDefault(int limitDefault) {
        this.limitDefault = limitDefault;
    }

    public int getKzInterval() {
        return kzInterval;
    }

    public void setKzInterval(int kzInterval) {
        this.kzInterval = kzInterval;
    }
}
