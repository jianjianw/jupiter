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

    /**
     * 订单比例
     */
    private String orderRate;

    /**
     * 套系比例
     */
    private String txRate;

    /**
     * 订单方式
     */
    private String orderStyleRate;

    /**
     * 付款方式
     */
    private String payStyleRate;


    public String getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(String orderRate) {
        this.orderRate = orderRate;
    }

    public String getTxRate() {
        return txRate;
    }

    public void setTxRate(String txRate) {
        this.txRate = txRate;
    }

    public String getOrderStyleRate() {
        return orderStyleRate;
    }

    public void setOrderStyleRate(String orderStyleRate) {
        this.orderStyleRate = orderStyleRate;
    }

    public String getPayStyleRate() {
        return payStyleRate;
    }

    public void setPayStyleRate(String payStyleRate) {
        this.payStyleRate = payStyleRate;
    }

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
