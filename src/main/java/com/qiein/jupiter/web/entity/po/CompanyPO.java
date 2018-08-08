package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 公司数据库对象
 */
public class CompanyPO extends BaseEntity {

    private static final long serialVersionUID = -6524936685705376975L;

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
    private int overTime;
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
    /**
     * 咨询类型
     */
    private boolean typeRepeat;
    /**
     * 渠道类型
     */
    private boolean srcRepeat;
    /**
     * 客资状态
     */
    private String statusIgnore;
    /**
     * 校验重复时间字段
     */
    private String timeTypeIgnore;
    /**
     * 校验重复时间天数
     */
    private Integer dayIgnore;

    /**
     * 非本人客资脱敏显示
     */
    private boolean notSelfBlind;

    /**
     * 个人中心不能自己上下线
     */
    private boolean unableSelfLine;
    /**
     * 电商录入时不能直接指定客服
     */
    private boolean unableAppointor;

    /**
     * 电商客资超时不能返无效
     */
    private int unableInvalidRange;

    private boolean kfEditJdRst;


    public boolean isKfEditJdRst() {
        return kfEditJdRst;
    }

    public void setKfEditJdRst(boolean kfEditJdRst) {
        this.kfEditJdRst = kfEditJdRst;
    }

    public boolean isTypeRepeat() {
        return typeRepeat;
    }

    public void setTypeRepeat(boolean typeRepeat) {
        this.typeRepeat = typeRepeat;
    }

    public boolean isSrcRepeat() {
        return srcRepeat;
    }

    public void setSrcRepeat(boolean srcRepeat) {
        this.srcRepeat = srcRepeat;
    }

    public String getStatusIgnore() {
        return statusIgnore;
    }

    public void setStatusIgnore(String statusIgnore) {
        this.statusIgnore = statusIgnore;
    }

    public String getTimeTypeIgnore() {
        return timeTypeIgnore;
    }

    public void setTimeTypeIgnore(String timeTypeIgnore) {
        this.timeTypeIgnore = timeTypeIgnore;
    }

    public Integer getDayIgnore() {
        return dayIgnore;
    }

    public void setDayIgnore(Integer dayIgnore) {
        this.dayIgnore = dayIgnore;
    }

    //
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

    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isNotSelfBlind() {
        return notSelfBlind;
    }

    public void setNotSelfBlind(boolean notSelfBlind) {
        this.notSelfBlind = notSelfBlind;
    }

    public boolean isUnableSelfLine() {
        return unableSelfLine;
    }

    public void setUnableSelfLine(boolean unableSelfLine) {
        this.unableSelfLine = unableSelfLine;
    }

    public boolean isUnableAppointor() {
        return unableAppointor;
    }

    public void setUnableAppointor(boolean unableAppointor) {
        this.unableAppointor = unableAppointor;
    }

    public int getUnableInvalidRange() {
        return unableInvalidRange;
    }

    public void setUnableInvalidRange(int unableInvalidRange) {
        this.unableInvalidRange = unableInvalidRange;
    }
}
