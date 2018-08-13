package com.qiein.jupiter.web.entity.vo;

/**
 * 渠道订单数据统计对象
 *
 * @Author: shiTao
 */
public class SourceOrderDataReportsVO {
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
    private int saleAvg;

    /**
     * 总花费
     */
    private int totalSpend;

    /**
     * 投入产出比
     */
    private double roi;

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

    public int getSaleAvg() {
        return saleAvg;
    }

    public void setSaleAvg(int saleAvg) {
        this.saleAvg = saleAvg;
    }

    public int getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(int totalSpend) {
        this.totalSpend = totalSpend;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }
}
