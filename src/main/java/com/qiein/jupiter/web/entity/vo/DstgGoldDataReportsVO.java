package com.qiein.jupiter.web.entity.vo;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public class DstgGoldDataReportsVO {
    /**
     * 广告id
     * */
    private String adId;

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
    private int waitClientCount;

    /**
     * 已预约数量
     * */
    private int appointClientCount;

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
    private Double validRate;

    /**
     * 无效率 (无效量 / 客资量)
     * */
    private Double inValidRate;

    /**
     * 待定率 (待定量 / 客资量)
     * */
    private Double waitRate;

    /**
     * 毛客资入店率 (入店量 / 总客资)
     * */
    private Double clientComeShopRate;

    /**
     * 有效客资入店率 (入店量 / 有效量 )
     * */
    private Double validClientComeShopRate;

    /**
     * 入店成交率(成交量 / 入店量)
     * */
    private Double comeShopSuccessRate;

    /**
     *  毛客资成交率 (成交量 / 总客资)
     * */
    private Double clientSuccessRate;

    /**
     * 有效客资成交率 (有效量 / 成交量)
     * */
    private Double validClientSuccessRate;

    /**
     * 成交均价
     * */
    private Double avgAmount;

    /**
     * 营业额
     * */
    private Double amount;


    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
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

    public int getWaitClientCount() {
        return waitClientCount;
    }

    public void setWaitClientCount(int waitClientCount) {
        this.waitClientCount = waitClientCount;
    }

    public int getAppointClientCount() {
        return appointClientCount;
    }

    public void setAppointClientCount(int appointClientCount) {
        this.appointClientCount = appointClientCount;
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

    public Double getValidRate() {
        return validRate;
    }

    public void setValidRate(Double validRate) {
        this.validRate = validRate;
    }

    public Double getInValidRate() {
        return inValidRate;
    }

    public void setInValidRate(Double inValidRate) {
        this.inValidRate = inValidRate;
    }

    public Double getWaitRate() {
        return waitRate;
    }

    public void setWaitRate(Double waitRate) {
        this.waitRate = waitRate;
    }

    public Double getClientComeShopRate() {
        return clientComeShopRate;
    }

    public void setClientComeShopRate(Double clientComeShopRate) {
        this.clientComeShopRate = clientComeShopRate;
    }

    public Double getValidClientComeShopRate() {
        return validClientComeShopRate;
    }

    public void setValidClientComeShopRate(Double validClientComeShopRate) {
        this.validClientComeShopRate = validClientComeShopRate;
    }

    public Double getComeShopSuccessRate() {
        return comeShopSuccessRate;
    }

    public void setComeShopSuccessRate(Double comeShopSuccessRate) {
        this.comeShopSuccessRate = comeShopSuccessRate;
    }

    public Double getClientSuccessRate() {
        return clientSuccessRate;
    }

    public void setClientSuccessRate(Double clientSuccessRate) {
        this.clientSuccessRate = clientSuccessRate;
    }

    public Double getValidClientSuccessRate() {
        return validClientSuccessRate;
    }

    public void setValidClientSuccessRate(Double validClientSuccessRate) {
        this.validClientSuccessRate = validClientSuccessRate;
    }

    public Double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(Double avgAmount) {
        this.avgAmount = avgAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
