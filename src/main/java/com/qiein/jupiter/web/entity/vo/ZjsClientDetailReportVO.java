package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class ZjsClientDetailReportVO implements Serializable {

    private static final long serialVersionUID = 1655993717406513184L;

    //可以存放客服id 和 客服组id
    private String id;

    //可以存放客服名 和 客服组名
    private String name;

    //毛客资数 = 总客资 - （筛选中，筛选无效，筛选待定）
    private int clientSourceCount;
    //有效客资数
    private int validClientSourceCount;
    //无效数
    private int invalidClientSourceCount;


    //总进店数
    private int totalInShopCount;
    //总成交数
    private int totalSuccessCount;
    //总成交率
    private double totalSuccessRate;

    //毛客资进店率
    private double clientInShopRate;
    //有效客资进店率
    private double validClientInShopRate;

    //周末进店数
    private int weekendInShopCount;
    //非周末进店数
    private int unWeekendInShopCount;

    //周末成交数
    private int weekendSuccessCount;
    //非周末成交数
    private int unWeekendSuccessCount;
    //非周末进店占比
    private double unWeekendInShopRate;

    //周末成交率
    private double weekendSuccessRate;
    //非周末成交率
    private double unWeekendSuccessRate;

    //营业额
    private double amount;
    //均价
    private double avgAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClientSourceCount() {
        return clientSourceCount;
    }

    public void setClientSourceCount(int clientSourceCount) {
        this.clientSourceCount = clientSourceCount;
    }

    public int getValidClientSourceCount() {
        return validClientSourceCount;
    }

    public void setValidClientSourceCount(int validClientSourceCount) {
        this.validClientSourceCount = validClientSourceCount;
    }

    public int getInvalidClientSourceCount() {
        return invalidClientSourceCount;
    }

    public void setInvalidClientSourceCount(int invalidClientSourceCount) {
        this.invalidClientSourceCount = invalidClientSourceCount;
    }

    public int getTotalInShopCount() {
        return totalInShopCount;
    }

    public void setTotalInShopCount(int totalInShopCount) {
        this.totalInShopCount = totalInShopCount;
    }

    public int getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(int totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }

    public double getTotalSuccessRate() {
        return totalSuccessRate;
    }

    public void setTotalSuccessRate(double totalSuccessRate) {
        this.totalSuccessRate = totalSuccessRate;
    }

    public double getClientInShopRate() {
        return clientInShopRate;
    }

    public void setClientInShopRate(double clientInShopRate) {
        this.clientInShopRate = clientInShopRate;
    }

    public double getValidClientInShopRate() {
        return validClientInShopRate;
    }

    public void setValidClientInShopRate(double validClientInShopRate) {
        this.validClientInShopRate = validClientInShopRate;
    }

    public int getWeekendInShopCount() {
        return weekendInShopCount;
    }

    public void setWeekendInShopCount(int weekendInShopCount) {
        this.weekendInShopCount = weekendInShopCount;
    }

    public int getUnWeekendInShopCount() {
        return unWeekendInShopCount;
    }

    public void setUnWeekendInShopCount(int unWeekendInShopCount) {
        this.unWeekendInShopCount = unWeekendInShopCount;
    }

    public int getWeekendSuccessCount() {
        return weekendSuccessCount;
    }

    public void setWeekendSuccessCount(int weekendSuccessCount) {
        this.weekendSuccessCount = weekendSuccessCount;
    }

    public int getUnWeekendSuccessCount() {
        return unWeekendSuccessCount;
    }

    public void setUnWeekendSuccessCount(int unWeekendSuccessCount) {
        this.unWeekendSuccessCount = unWeekendSuccessCount;
    }

    public double getUnWeekendInShopRate() {
        return unWeekendInShopRate;
    }

    public void setUnWeekendInShopRate(double unWeekendInShopRate) {
        this.unWeekendInShopRate = unWeekendInShopRate;
    }

    public double getWeekendSuccessRate() {
        return weekendSuccessRate;
    }

    public void setWeekendSuccessRate(double weekendSuccessRate) {
        this.weekendSuccessRate = weekendSuccessRate;
    }

    public double getUnWeekendSuccessRate() {
        return unWeekendSuccessRate;
    }

    public void setUnWeekendSuccessRate(double unWeekendSuccessRate) {
        this.unWeekendSuccessRate = unWeekendSuccessRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(double avgAmount) {
        this.avgAmount = avgAmount;
    }
}
