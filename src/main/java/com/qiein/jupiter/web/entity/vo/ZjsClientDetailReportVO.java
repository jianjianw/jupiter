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

    //A类客资数   yx_level  123   对应abc
    private int clientSourceLevelACount;
    //A类客资数进店数
    private int clientSourceLevelAInShopCount;
    //A类客资转化率
    private double clientSourceLevelARate;

    //B类客资数
    private int clientSourceLevelBCount;
    //B类客资数进店数
    private int clientSourceLevelBInShopCount;
    //B类客资转化率
    private double clientSourceLevelBRate;


    //C类客资数
    private int clientSourceLevelCCount;
    //C类客资数进店数
    private int clientSourceLevelCInShopCount;
    //C类客资转化率
    private double clientSourceLevelCRate;

    //D类客资数
    private int clientSourceLevelDCount;
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
    private int unWeekendInShopRate;

    //周末成交率
    private int weekendSuccessRate;
    //非周末成交率
    private int unWeekendSuccessRate;

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

    public int getClientSourceLevelACount() {
        return clientSourceLevelACount;
    }

    public void setClientSourceLevelACount(int clientSourceLevelACount) {
        this.clientSourceLevelACount = clientSourceLevelACount;
    }

    public int getClientSourceLevelAInShopCount() {
        return clientSourceLevelAInShopCount;
    }

    public void setClientSourceLevelAInShopCount(int clientSourceLevelAInShopCount) {
        this.clientSourceLevelAInShopCount = clientSourceLevelAInShopCount;
    }

    public double getClientSourceLevelARate() {
        return clientSourceLevelARate;
    }

    public void setClientSourceLevelARate(double clientSourceLevelARate) {
        this.clientSourceLevelARate = clientSourceLevelARate;
    }

    public int getClientSourceLevelBCount() {
        return clientSourceLevelBCount;
    }

    public void setClientSourceLevelBCount(int clientSourceLevelBCount) {
        this.clientSourceLevelBCount = clientSourceLevelBCount;
    }

    public int getClientSourceLevelBInShopCount() {
        return clientSourceLevelBInShopCount;
    }

    public void setClientSourceLevelBInShopCount(int clientSourceLevelBInShopCount) {
        this.clientSourceLevelBInShopCount = clientSourceLevelBInShopCount;
    }

    public double getClientSourceLevelBRate() {
        return clientSourceLevelBRate;
    }

    public void setClientSourceLevelBRate(double clientSourceLevelBRate) {
        this.clientSourceLevelBRate = clientSourceLevelBRate;
    }

    public int getClientSourceLevelCCount() {
        return clientSourceLevelCCount;
    }

    public void setClientSourceLevelCCount(int clientSourceLevelCCount) {
        this.clientSourceLevelCCount = clientSourceLevelCCount;
    }

    public int getClientSourceLevelCInShopCount() {
        return clientSourceLevelCInShopCount;
    }

    public void setClientSourceLevelCInShopCount(int clientSourceLevelCInShopCount) {
        this.clientSourceLevelCInShopCount = clientSourceLevelCInShopCount;
    }

    public double getClientSourceLevelCRate() {
        return clientSourceLevelCRate;
    }

    public void setClientSourceLevelCRate(double clientSourceLevelCRate) {
        this.clientSourceLevelCRate = clientSourceLevelCRate;
    }

    public int getClientSourceLevelDCount() {
        return clientSourceLevelDCount;
    }

    public void setClientSourceLevelDCount(int clientSourceLevelDCount) {
        this.clientSourceLevelDCount = clientSourceLevelDCount;
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

    public int getUnWeekendInShopRate() {
        return unWeekendInShopRate;
    }

    public void setUnWeekendInShopRate(int unWeekendInShopRate) {
        this.unWeekendInShopRate = unWeekendInShopRate;
    }

    public int getWeekendSuccessRate() {
        return weekendSuccessRate;
    }

    public void setWeekendSuccessRate(int weekendSuccessRate) {
        this.weekendSuccessRate = weekendSuccessRate;
    }

    public int getUnWeekendSuccessRate() {
        return unWeekendSuccessRate;
    }

    public void setUnWeekendSuccessRate(int unWeekendSuccessRate) {
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
