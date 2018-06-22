package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 公司数据库对象
 */
public class CompanyPO extends BaseEntity {

    private static final long serialVersionUID = -6524936685705376975L;
    /**
     * 数据库字段
     */
    public static final String COLUMN_UNABLEINVALIDRANGE = "UNABLEINVALIDRANGE";// 电商客资超过指定时间不能返无效
    public static final String COLUMN_UNABLEAPPOINTOR = "UNABLEAPPOINTOR";// 电商客资录入时不能直接指定客服
    public static final String COLUMN_UNABLESELFLINE = "UNABLESELFLINE";// 员工个人不能自己操作上线下线
    public static final String COLUMN_NOTSELFBLIND = "NOTSELFBLIND";// 非自己客资脱敏显示
    public static final String COLUMN_KZINTERVAL = "KZINTERVAL";// 客服领取客资多少时间后不能领取下一个
    public static final String COLUMN_LIMITDEFAULT = "LIMITDEFAULT";// 客服日领单默认最单限额
    public static final String COLUMN_OVERTIME = "OVERTIME";// 客服领取客资超时时间
    public static final String COLUMN_SSOLIMIT = "SSOLIMIT";// 是否允许单账号同时多端在线

    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司类型
     */
    private int typeId;
    /**
     * logo
     */
    private String logo;
    /**
     * 钉钉关联ID
     */
    private String corpId;
    /**
     * 自定义配置
     */
    private String config;
    /**
     * 是否开启IP校验
     */
    private boolean ipLimit;
    /**
     * 是否开启单点登录
     */
    private boolean ssoLimit;
    /**
     * 领取客资超时时间
     */
    private int overtime;
    /**
     * 默认每日接单限额
     */
    private int limitDefault;
    /**
     * 客资领取后多少时间不能领取下一个
     */
    private int kzInterval;
    /**
     * 是否删除
     */
    private boolean delFlag;
    /**
     * 只允许客户端
     */
    private boolean onlyApp;
    /**
     * 是否锁定
     */
    private boolean lockFlag;

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public boolean isOnlyApp() {
        return onlyApp;
    }

    public void setOnlyApp(boolean onlyApp) {
        this.onlyApp = onlyApp;
    }

    public boolean isLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getCompanyName() {
        return companyName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
