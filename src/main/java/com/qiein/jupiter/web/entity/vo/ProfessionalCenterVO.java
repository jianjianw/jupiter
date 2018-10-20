package com.qiein.jupiter.web.entity.vo;

/**
 * 专业中心报表
 * author:xiangliang
 */
public class ProfessionalCenterVO {
    /**
     * 渠道id
     */
    private Integer srcId;
    /**
     * 渠道名称
     */
    private String srcName;
    /**
     * 渠道图片
     */
    private String srcImg;
    /**
     * 总拍摄目标
     */
    private int totalShootingTarget;
    /**
     * 有效客资量目标
     */
    private int validCountTarget;
    /**
     * 总拍摄量
     */
    private int totalShooting;
    /**
     * 总拍摄目标完成率
     */
    private double totalShootingTargetRate;
    /**
     * 有效客资挖客率目标
     */
    private double validCountWkRateTarget;
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
     * 有效客资目标完成率
     */
    private double validCountTargetRate;
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
    /**
     * 成交量
     */
    private int successClientCount;

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public int getTotalShootingTarget() {
        return totalShootingTarget;
    }

    public void setTotalShootingTarget(int totalShootingTarget) {
        this.totalShootingTarget = totalShootingTarget;
    }

    public int getValidCountTarget() {
        return validCountTarget;
    }

    public void setValidCountTarget(int validCountTarget) {
        this.validCountTarget = validCountTarget;
    }

    public int getTotalShooting() {
        return totalShooting;
    }

    public void setTotalShooting(int totalShooting) {
        this.totalShooting = totalShooting;
    }

    public double getTotalShootingTargetRate() {
        return totalShootingTargetRate;
    }

    public void setTotalShootingTargetRate(double totalShootingTargetRate) {
        this.totalShootingTargetRate = totalShootingTargetRate;
    }

    public double getValidCountWkRateTarget() {
        return validCountWkRateTarget;
    }

    public void setValidCountWkRateTarget(double validCountWkRateTarget) {
        this.validCountWkRateTarget = validCountWkRateTarget;
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

    public double getValidCountTargetRate() {
        return validCountTargetRate;
    }

    public void setValidCountTargetRate(double validCountTargetRate) {
        this.validCountTargetRate = validCountTargetRate;
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

    public int getSuccessClientCount() {
        return successClientCount;
    }

    public void setSuccessClientCount(int successClientCount) {
        this.successClientCount = successClientCount;
    }
}
