package com.qiein.jupiter.web.entity.vo;

/**
 * author:xiangliang
 * 销售中心报表
 */
public class SalesCenterReportsVO {
    /**
     * 门店id
     */
    private int shopId;
    /**
     * 门店名称
     */
    private String shopName;
    /**
     * 总成交量目标
     */
    private int totalSuccessCountTarget;
    /**
     * 门店指名客资成交量目标
     */
    private int shopCallOnSuccessCountTarget;
    /**
     * 门店指名有效客资量目标
     */
    private int shopCallOnValidCountTarget;
    /**
     * 有效客资挖客率目标
     */
    private double validCountWkRateTarget;
    /**
     * 成交量
     */
    private int successClientCount;
    /**
     * 门市指名成交量
     */
    private int shopCallOnSuccessClientCount;
    /**
     * 总客资
     */
    private int allClientCount;
    /**
     * 毛客资量
     */
    private int clientCount;
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

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getTotalSuccessCountTarget() {
        return totalSuccessCountTarget;
    }

    public void setTotalSuccessCountTarget(int totalSuccessCountTarget) {
        this.totalSuccessCountTarget = totalSuccessCountTarget;
    }

    public int getShopCallOnSuccessCountTarget() {
        return shopCallOnSuccessCountTarget;
    }

    public void setShopCallOnSuccessCountTarget(int shopCallOnSuccessCountTarget) {
        this.shopCallOnSuccessCountTarget = shopCallOnSuccessCountTarget;
    }

    public int getShopCallOnValidCountTarget() {
        return shopCallOnValidCountTarget;
    }

    public void setShopCallOnValidCountTarget(int shopCallOnValidCountTarget) {
        this.shopCallOnValidCountTarget = shopCallOnValidCountTarget;
    }

    public double getValidCountWkRateTarget() {
        return validCountWkRateTarget;
    }

    public void setValidCountWkRateTarget(double validCountWkRateTarget) {
        this.validCountWkRateTarget = validCountWkRateTarget;
    }

    public int getSuccessClientCount() {
        return successClientCount;
    }

    public void setSuccessClientCount(int successClientCount) {
        this.successClientCount = successClientCount;
    }

    public int getShopCallOnSuccessClientCount() {
        return shopCallOnSuccessClientCount;
    }

    public void setShopCallOnSuccessClientCount(int shopCallOnSuccessClientCount) {
        this.shopCallOnSuccessClientCount = shopCallOnSuccessClientCount;
    }

    public int getAllClientCount() {
        return allClientCount;
    }

    public void setAllClientCount(int allClientCount) {
        this.allClientCount = allClientCount;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
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
