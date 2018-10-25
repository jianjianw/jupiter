package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * FileName: DsyyReportsVO
 *
 * @author: yyx
 * @Date: 2018-7-6 10:07
 */
public class DsyyReportsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 组id
     * */
    private String groupId;
    /**
     * 组名称
     * */
    private String groupName;

    /**
     * 总客资量
     * */
    private int allClientCount;

    /**
     * 客资量
     * */
    private  int clientCount;

    /**
     * 有效量
     * */
    private int validClientCount;

    /**
     * 无效量
     * */
    private int invalidClientCount;

    /**
     * 待定量
     * */
    private int waitClientCount;

    /**
     * 筛选待定
     * */
    private int filterWaitClientCount;

    /**
     * 筛选中
     * */
    private int inFilterClientCount;

    /**
     * 筛选无效
     * */
    private int InvalidFilterClientCount;

    /**
     * 入店量
     * */
    private int comeShopClientCount;

    /**
     * 周末入店量
     * */
    private int weekComeShopClientCount;

    /**
     * 非周末入店量
     * */
    private int notWeekComeShopClientCount;

    /**
     * 成交量
     * */
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
     * 有效率
     * */
    private double validRate;

    /**
     * 毛客资入店率
     * */
    private double clientComeShopRate;

    /**
     * 有效客资入店率
     * */
    private double validComeShopRate;

    /**
     * 毛客资成交率
     * */
    private double clientSuccessRate;

    /**
     * 有效客资成交率
     * */
    private double successValidRate;

    /**
     * 入店成交率
     * */
    private double comeShopSuccessRate;

    /**
     * 非周末入店占比
     * */
    private double notWeekComeShopRate;

    /**
     * 客资花费
     * */
    private double clientCost;

    /**
     * 毛客资成本
     * */
    private double groupClientCost;

    /**
     * 营业额
     * */
    private double sumAmount;
    /**
     * 实收金额
     */
    private double realAmount;
    /**
     * 补款
     */
    private double bkAmount;
    /**
     * 已收金额
     * */
    private double stayAmount;

    /**
     * 成交均价
     * */
    private double avgAmount;

    /**
     * ROI
     * */
    private double roi;

    /**
     * 网转客资量
     * */
    private int netTurnClientCount;

    /**
     * 网销待定客资量
     * */
    private int netTurnWaitClientCount;

    /**
     * 网销转介绍无效客资
     * */
    private int netTurnInValidClientCount;

    /**
     * 网销转介绍入店量
     * */
    private int netTurnComeShopClientCount;

    /**
     *  已加微信数
     * */
    private int successWeChat;

    /**
     * 入店成本
     */
    private double comeShopCost;

    /**
     * 订单成本
     */
    private double successCost;

    public double getComeShopCost() {
        return comeShopCost;
    }

    public void setComeShopCost(double comeShopCost) {
        this.comeShopCost = comeShopCost;
    }

    public double getSuccessCost() {
        return successCost;
    }

    public void setSuccessCost(double successCost) {
        this.successCost = successCost;
    }

    public double getBkAmount() {
        return bkAmount;
    }

    public void setBkAmount(double bkAmount) {
        this.bkAmount = bkAmount;
    }

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

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public double getStayAmount() {
        return stayAmount;
    }

    public void setStayAmount(double stayAmount) {
        this.stayAmount = stayAmount;
    }

    public int getSuccessWeChat() {
        return successWeChat;
    }

    public void setSuccessWeChat(int successWeChat) {
        this.successWeChat = successWeChat;
    }

    public int getNetTurnComeShopClientCount() {
        return netTurnComeShopClientCount;
    }

    public void setNetTurnComeShopClientCount(int netTurnComeShopClientCount) {
        this.netTurnComeShopClientCount = netTurnComeShopClientCount;
    }

    public int getNetTurnInValidClientCount() {
        return netTurnInValidClientCount;
    }

    public void setNetTurnInValidClientCount(int netTurnInValidClientCount) {
        this.netTurnInValidClientCount = netTurnInValidClientCount;
    }

    public int getNetTurnWaitClientCount() {
        return netTurnWaitClientCount;
    }

    public void setNetTurnWaitClientCount(int netTurnWaitClientCount) {
        this.netTurnWaitClientCount = netTurnWaitClientCount;
    }

    public int getNetTurnClientCount() {
        return netTurnClientCount;
    }

    public void setNetTurnClientCount(int netTurnClientCount) {
        this.netTurnClientCount = netTurnClientCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public int getInvalidClientCount() {
        return invalidClientCount;
    }

    public void setInvalidClientCount(int invalidClientCount) {
        this.invalidClientCount = invalidClientCount;
    }

    public int getWaitClientCount() {
        return waitClientCount;
    }

    public void setWaitClientCount(int waitClientCount) {
        this.waitClientCount = waitClientCount;
    }

    public int getFilterWaitClientCount() {
        return filterWaitClientCount;
    }

    public void setFilterWaitClientCount(int filterWaitClientCount) {
        this.filterWaitClientCount = filterWaitClientCount;
    }

    public int getInFilterClientCount() {
        return inFilterClientCount;
    }

    public void setInFilterClientCount(int inFilterClientCount) {
        this.inFilterClientCount = inFilterClientCount;
    }

    public int getInvalidFilterClientCount() {
        return InvalidFilterClientCount;
    }

    public void setInvalidFilterClientCount(int invalidFilterClientCount) {
        InvalidFilterClientCount = invalidFilterClientCount;
    }

    public int getComeShopClientCount() {
        return comeShopClientCount;
    }

    public void setComeShopClientCount(int comeShopClientCount) {
        this.comeShopClientCount = comeShopClientCount;
    }

    public int getWeekComeShopClientCount() {
        return weekComeShopClientCount;
    }

    public void setWeekComeShopClientCount(int weekComeShopClientCount) {
        this.weekComeShopClientCount = weekComeShopClientCount;
    }

    public int getNotWeekComeShopClientCount() {
        return notWeekComeShopClientCount;
    }

    public void setNotWeekComeShopClientCount(int notWeekComeShopClientCount) {
        this.notWeekComeShopClientCount = notWeekComeShopClientCount;
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

    public double getClientComeShopRate() {
        return clientComeShopRate;
    }

    public void setClientComeShopRate(double clientComeShopRate) {
        this.clientComeShopRate = clientComeShopRate;
    }

    public double getValidComeShopRate() {
        return validComeShopRate;
    }

    public void setValidComeShopRate(double validComeShopRate) {
        this.validComeShopRate = validComeShopRate;
    }

    public double getClientSuccessRate() {
        return clientSuccessRate;
    }

    public void setClientSuccessRate(double clientSuccessRate) {
        this.clientSuccessRate = clientSuccessRate;
    }

    public double getSuccessValidRate() {
        return successValidRate;
    }

    public void setSuccessValidRate(double successValidRate) {
        this.successValidRate = successValidRate;
    }

    public double getComeShopSuccessRate() {
        return comeShopSuccessRate;
    }

    public void setComeShopSuccessRate(double comeShopSuccessRate) {
        this.comeShopSuccessRate = comeShopSuccessRate;
    }

    public double getNotWeekComeShopRate() {
        return notWeekComeShopRate;
    }

    public void setNotWeekComeShopRate(double notWeekComeShopRate) {
        this.notWeekComeShopRate = notWeekComeShopRate;
    }

    public double getClientCost() {
        return clientCost;
    }

    public void setClientCost(double clientCost) {
        this.clientCost = clientCost;
    }

    public double getGroupClientCost() {
        return groupClientCost;
    }

    public void setGroupClientCost(double groupClientCost) {
        this.groupClientCost = groupClientCost;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(double sumAmount) {
        this.sumAmount = sumAmount;
    }

    public double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(double avgAmount) {
        this.avgAmount = avgAmount;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }
}
