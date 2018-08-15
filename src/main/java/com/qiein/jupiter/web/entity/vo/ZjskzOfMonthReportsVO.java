package com.qiein.jupiter.web.entity.vo;

public class ZjskzOfMonthReportsVO {
    /**
     * 日期信息
     * */
    private String dayId;

    /**
     * 总客资
     * */
    private int allClientCount;

    /**
     * 客资量
     * */
    private int clientCount;

    /**
     * 有效量
     * */
    private int validClientCount;

    /**
     * 无效量
     * */
    private int inValidClientCount;

    /**
     * 待定量
     * */
    private int pendingClientCount;

    /**
     * 筛选待定
     * */
    private int filterPendingClientCount;

    /**
     * 筛选中
     * */
    private int filterInClientCount;

    /**
     * 筛选无效
     * */
    private int filterInValidClientCount;


    /**
     * 入店量
     * */
    private int comeShopClientCount;

    /**
     * 成交量
     * */
    private int successClientCount;

    /**
     * 有效率 (有效量 / 客资量)
     * */
    private double validRate;

    /**
     * 无效率 (无效量 / 客资量)
     * */
    private double inValidRate;

    /**
     * 待定率 (待定量 / 客资量)
     * */
    private double waitRate;

    /**
     * 毛客资入店率 (入店量 / 总客资)
     * */
    private double clientComeShopRate;

    /**
     * 有效客资入店率 (入店量 / 有效量 )
     * */
    private double validClientComeShopRate;

    /**
     * 入店成交率(成交量 / 入店量)
     * */
    private double comeShopSuccessRate;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
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

    public int getInValidClientCount() {
        return inValidClientCount;
    }

    public void setInValidClientCount(int inValidClientCount) {
        this.inValidClientCount = inValidClientCount;
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

    public int getComeShopClientCount() {
        return comeShopClientCount;
    }

    public void setComeShopClientCount(int comeShopClientCount) {
        this.comeShopClientCount = comeShopClientCount;
    }

    public int getSuccessClientCount() {
        return successClientCount;
    }

    public void setSuccessClientCount(int successClientCount) {
        this.successClientCount = successClientCount;
    }

    public double getValidRate() {
        return validRate;
    }

    public void setValidRate(double validRate) {
        this.validRate = validRate;
    }

    public double getInValidRate() {
        return inValidRate;
    }

    public void setInValidRate(double inValidRate) {
        this.inValidRate = inValidRate;
    }

    public double getWaitRate() {
        return waitRate;
    }

    public void setWaitRate(double waitRate) {
        this.waitRate = waitRate;
    }

    public double getClientComeShopRate() {
        return clientComeShopRate;
    }

    public void setClientComeShopRate(double clientComeShopRate) {
        this.clientComeShopRate = clientComeShopRate;
    }

    public double getValidClientComeShopRate() {
        return validClientComeShopRate;
    }

    public void setValidClientComeShopRate(double validClientComeShopRate) {
        this.validClientComeShopRate = validClientComeShopRate;
    }

    public double getComeShopSuccessRate() {
        return comeShopSuccessRate;
    }

    public void setComeShopSuccessRate(double comeShopSuccessRate) {
        this.comeShopSuccessRate = comeShopSuccessRate;
    }
}
