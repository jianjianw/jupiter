package com.qiein.jupiter.web.entity.vo;

/**
 * author:xiangliang
 * 销售中心报表
 */
public class SalesCenterReportsVO {
    /**
     * 门店id
     */
    private Integer shopId;
    /**
     * 门店名称
     */
    private String shopName;
    /**
     * 总成交量目标
     */
    private Integer totalSuccessCountTarget;
    /**
     * 门店指名客资成交量目标
     */
    private Integer shopCallOnSuccessCountTarget;
    /**
     * 门店指名有效客资量目标
     */
    private double shopCallOnValidCountTarget;
    /**
     * 有效客资挖客率目标
     */
    private double validCountWkRateTarget;
    /**
     * 成交量
     */
    private Integer successClientCount;
    /**
     * 门市指名成交量
     */
    private Integer shopCallOnSuccessClientCount;
    /**
     * 总客资
     */
    private Integer allClientCount;
    /**
     * 毛客资量
     */
    private Integer clientCount;
    /**
     * 有效量
     */
    private int validClientCount;

    /**
     * 待定量
     */
    private int pendingClientCount;

    /**
     * 筛选待定
     */
    private int filterPendingClientCount;

    /**
     * 筛选中
     */
    private int filterInClientCount;

    /**
     * 筛选无效
     */
    private int filterInValidClientCount;
    /**
     * 总成交目标完成率
     */
    private double totalSuccessClientTargetRate;
    /**
     * 门市指名总成交目标完成率
     */
    private double shopCallOnSuccessClientTargetRate;
    /**
     * 有效客资目标完成率
     */
    private double shopCallOnValidCountTargetRate;
    /**
     * 待定率
     */
    private double pendingClientRate;
    /**
     * 有效率
     */
    private double validClientRate;
    /**
     * 有效客资挖客率
     */
    private double validCountWkRate;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getTotalSuccessCountTarget() {
        return totalSuccessCountTarget;
    }

    public void setTotalSuccessCountTarget(Integer totalSuccessCountTarget) {
        this.totalSuccessCountTarget = totalSuccessCountTarget;
    }

    public Integer getShopCallOnSuccessCountTarget() {
        return shopCallOnSuccessCountTarget;
    }

    public void setShopCallOnSuccessCountTarget(Integer shopCallOnSuccessCountTarget) {
        this.shopCallOnSuccessCountTarget = shopCallOnSuccessCountTarget;
    }

    public double getShopCallOnValidCountTarget() {
        return shopCallOnValidCountTarget;
    }

    public void setShopCallOnValidCountTarget(double shopCallOnValidCountTarget) {
        this.shopCallOnValidCountTarget = shopCallOnValidCountTarget;
    }

    public double getValidCountWkRateTarget() {
        return validCountWkRateTarget;
    }

    public void setValidCountWkRateTarget(double validCountWkRateTarget) {
        this.validCountWkRateTarget = validCountWkRateTarget;
    }

    public Integer getSuccessClientCount() {
        return successClientCount;
    }

    public void setSuccessClientCount(Integer successClientCount) {
        this.successClientCount = successClientCount;
    }

    public Integer getShopCallOnSuccessClientCount() {
        return shopCallOnSuccessClientCount;
    }

    public void setShopCallOnSuccessClientCount(Integer shopCallOnSuccessClientCount) {
        this.shopCallOnSuccessClientCount = shopCallOnSuccessClientCount;
    }

    public Integer getAllClientCount() {
        return allClientCount;
    }

    public void setAllClientCount(Integer allClientCount) {
        this.allClientCount = allClientCount;
    }

    public Integer getClientCount() {
        return clientCount;
    }

    public void setClientCount(Integer clientCount) {
        this.clientCount = clientCount;
    }

    public int getValidClientCount() {
        return validClientCount;
    }

    public void setValidClientCount(int validClientCount) {
        this.validClientCount = validClientCount;
    }

    public int getPendingClientCount() {
        return pendingClientCount;
    }

    public void setPendingClientCount(int pendingClientCount) {
        this.pendingClientCount = pendingClientCount;
    }

    public int getFilterPendingClientCount() {
        return filterPendingClientCount;
    }

    public void setFilterPendingClientCount(int filterPendingClientCount) {
        this.filterPendingClientCount = filterPendingClientCount;
    }

    public int getFilterInClientCount() {
        return filterInClientCount;
    }

    public void setFilterInClientCount(int filterInClientCount) {
        this.filterInClientCount = filterInClientCount;
    }

    public int getFilterInValidClientCount() {
        return filterInValidClientCount;
    }

    public void setFilterInValidClientCount(int filterInValidClientCount) {
        this.filterInValidClientCount = filterInValidClientCount;
    }

    public double getTotalSuccessClientTargetRate() {
        return totalSuccessClientTargetRate;
    }

    public void setTotalSuccessClientTargetRate(double totalSuccessClientTargetRate) {
        this.totalSuccessClientTargetRate = totalSuccessClientTargetRate;
    }

    public double getShopCallOnSuccessClientTargetRate() {
        return shopCallOnSuccessClientTargetRate;
    }

    public void setShopCallOnSuccessClientTargetRate(double shopCallOnSuccessClientTargetRate) {
        this.shopCallOnSuccessClientTargetRate = shopCallOnSuccessClientTargetRate;
    }

    public double getShopCallOnValidCountTargetRate() {
        return shopCallOnValidCountTargetRate;
    }

    public void setShopCallOnValidCountTargetRate(double shopCallOnValidCountTargetRate) {
        this.shopCallOnValidCountTargetRate = shopCallOnValidCountTargetRate;
    }

    public double getPendingClientRate() {
        return pendingClientRate;
    }

    public void setPendingClientRate(double pendingClientRate) {
        this.pendingClientRate = pendingClientRate;
    }

    public double getValidClientRate() {
        return validClientRate;
    }

    public void setValidClientRate(double validClientRate) {
        this.validClientRate = validClientRate;
    }

    public double getValidCountWkRate() {
        return validCountWkRate;
    }

    public void setValidCountWkRate(double validCountWkRate) {
        this.validCountWkRate = validCountWkRate;
    }
}
