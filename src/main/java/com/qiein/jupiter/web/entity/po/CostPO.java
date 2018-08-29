package com.qiein.jupiter.web.entity.po;

import java.math.BigDecimal;

/**
 * 花费信息
 * author xiangliang 2018/6/22
 */
public class CostPO {
    private Integer id;
    private BigDecimal beforeCost;// 实际金额
    private BigDecimal afterCost;//花费金额
    private Integer srcId;// 渠道id
    private String costTime;// 日期
    private String dayKey;
    private BigDecimal rate;
    private Integer companyId;
    private Integer time;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getDayKey() {
        return dayKey;
    }

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }

    public BigDecimal getBeforeCost() {
        return beforeCost;
    }

    public void setBeforeCost(BigDecimal beforeCost) {
        this.beforeCost = beforeCost;
    }

    public BigDecimal getAfterCost() {
        return afterCost;
    }

    public void setAfterCost(BigDecimal afterCost) {
        this.afterCost = afterCost;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }
}
