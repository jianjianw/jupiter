package com.qiein.jupiter.web.entity.vo;

public class OldKzReportsVO {
    /**
     * 老客电话
     */
    private String oldKzPhone;
    /**
     * 老客姓名
     */
    private String oldKzName;
    /**
     * 总客资
     */
    private int allClientCount;

    /**
     * 客资量
     */
    private int clientCount;

    /**
     * 有效量
     */
    private int validClientCount;

    /**
     * 无效量
     */
    private int inValidClientCount;

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
     * 入店量
     */
    private int comeShopClientCount;

    /**
     * 成交量
     */
    private int successClientCount;
    /**
     * 入店成交量
     */
    private int comeShopSuccessClientCount;
    /**
     * 在线成交量
     */
    private int onLineSuccessClientCount;
    /**
     * 有效率 (有效量 / 客资量)
     */
    private double validRate;

    /**
     * 无效率 (无效量 / 客资量)
     */
    private double inValidRate;

    /**
     * 待定率 (待定量 / 客资量)
     */
    private double waitRate;

    /**
     * 毛客资入店率 (入店量 / 总客资)
     */
    private double clientComeShopRate;

    /**
     * 有效客资入店率 (入店量 / 有效量 )
     */
    private double validClientComeShopRate;

    /**
     * 入店成交率(成交量 / 入店量)
     */
    private double comeShopSuccessRate;

    /**
     * 毛客资成交率 (成交量 / 总客资)
     */
    private double clientSuccessRate;

    /**
     * 有效客资成交率 (有效量 / 成交量)
     */
    private double validClientSuccessRate;

    /**
     * 成交均价
     */
    private double avgAmount;

    /**
     * 营业额
     */
    private double amount;

    public int getComeShopSuccessClientCount() {
        return comeShopSuccessClientCount;
    }

    public void setComeShopSuccessClientCount(int comeShopSuccessClientCount) {
        this.comeShopSuccessClientCount = comeShopSuccessClientCount;
    }

    public int getOnLineSuccessClientCount() {
        return onLineSuccessClientCount;
    }

    public void setOnLineSuccessClientCount(int onLineSuccessClientCount) {
        this.onLineSuccessClientCount = onLineSuccessClientCount;
    }

    public String getOldKzPhone() {
        return oldKzPhone;
    }

    public void setOldKzPhone(String oldKzPhone) {
        this.oldKzPhone = oldKzPhone;
    }

    public String getOldKzName() {
        return oldKzName;
    }

    public void setOldKzName(String oldKzName) {
        this.oldKzName = oldKzName;
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

    public double getClientSuccessRate() {
        return clientSuccessRate;
    }

    public void setClientSuccessRate(double clientSuccessRate) {
        this.clientSuccessRate = clientSuccessRate;
    }

    public double getValidClientSuccessRate() {
        return validClientSuccessRate;
    }

    public void setValidClientSuccessRate(double validClientSuccessRate) {
        this.validClientSuccessRate = validClientSuccessRate;
    }

    public double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(double avgAmount) {
        this.avgAmount = avgAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
