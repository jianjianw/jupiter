package com.qiein.jupiter.web.entity.vo;

import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-17
 */
public class DstgYearDetailReportsVO {
    /**
     * 总客资量
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
     * 有效率
     * */
    private double validRate;

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
     * 总花费
     * */
    private double allCost;

    /**
     * 毛客资成本
     * */
    private double clientCost;

    /**
     * 有效成本
     * */
    private double validClientCost;

    /**
     * 月份
     * */
    private String month;


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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public double getValidRate() {
        return validRate;
    }

    public void setValidRate(double validRate) {
        this.validRate = validRate;
    }

    public double getAllCost() {
        return allCost;
    }

    public void setAllCost(double allCost) {
        this.allCost = allCost;
    }

    public double getClientCost() {
        return clientCost;
    }

    public void setClientCost(double clientCost) {
        this.clientCost = clientCost;
    }

    public double getValidClientCost() {
        return validClientCost;
    }

    public void setValidClientCost(double validClientCost) {
        this.validClientCost = validClientCost;
    }

}
