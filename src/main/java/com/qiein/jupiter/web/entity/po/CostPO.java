package com.qiein.jupiter.web.entity.po;

import java.math.BigDecimal;

/**
 * 花费信息
 * author xiangliang 2018/6/22
 */
public class CostPO {
    private BigDecimal beforeCost;// 花费金额
    private BigDecimal afterCost;//实际金额
    private Integer srcId;// 渠道id
    private String costTime;// 日期

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
