package com.qiein.jupiter.web.entity.vo;

/**
 * 渠道订单数据统计对象
 *
 * @Author: shiTao
 */
public class SourceOrderDataReportsVO {
    /**
     * 类型
     */
    private int srcType;
    /**
     * 类型名称
     */
    private String srcName;
    /**
     * 订单总量
     */
    private int orderAmount;
    /**
     * 套系总金额
     */
    private int txTotalAmount;
    /**
     * 总已收金额
     */
    private int totalYsAmount;
    /**
     * 销售均价
     */
    private String saleAvg;

    /**
     * 总花费
     */
    private int totalSpend;

    /**
     * 投入产出比
     */
    private String roi;


    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public int getSrcType() {
        return srcType;
    }

    public void setSrcType(int srcType) {
        this.srcType = srcType;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getTxTotalAmount() {
        return txTotalAmount;
    }

    public void setTxTotalAmount(int txTotalAmount) {
        this.txTotalAmount = txTotalAmount;
    }

    public int getTotalYsAmount() {
        return totalYsAmount;
    }

    public void setTotalYsAmount(int totalYsAmount) {
        this.totalYsAmount = totalYsAmount;
    }

    public String getSaleAvg() {
        return saleAvg;
    }

    public void setSaleAvg(String saleAvg) {
        this.saleAvg = saleAvg;
    }

    public int getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(int totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getRoi() {
        return roi;
    }

    public void setRoi(String roi) {
        this.roi = roi;
    }
}
