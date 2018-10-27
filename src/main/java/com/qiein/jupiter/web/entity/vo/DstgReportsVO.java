package com.qiein.jupiter.web.entity.vo;

/**
 * author :xiangliang
 */
public class DstgReportsVO {
    /**
     * 来源id
     * */
    private int id;
    /**
     * 来源名称
     * */
    private String srcName;
    /**
     * 总客资量
     * */
    private int allClientCount;
    /**
     * 客资量
     * */
    private int unAllClientCount;
    /**
     *  有效客资量
     * */
    private int validClientCount;
    /**
     * 待定客资量
     * */
    private int waitClientCount;
    /**
     * 无效客资量
     * */
    private int inValidClientCount;
    /**
     * 预约量
     * */
    private int appointClientCount;
    /**
     * 不包含待跟踪预约量
     * */
    private int appointClientCountNotContains;
    /**
     * 创建时间内不包含待跟踪预约量
     * */
    private int appointClientCountNotContainsAndCreateTime;
    /**
     * 入店量
     * */
    private int comeShopClientCount;
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
     * 指定时间内成交量
     * */
    private int specifiedTimeClientCount;
    /**
     * 有效率
     * */
    private Double validRate;
    /**
     * 筛选率
     * */
    private Double waitRate;
    /**
     * 无效率
     * */
    private Double inValidRate;
    /**
     * 毛客资入店率 = 入店量 / 客资量
     * */
    private Double appointRate;
    /**
     * 有效客资入店率 = 入店量 / 有效量
     * */
    private Double validAppointRate;
    /**
     * 毛客资成交率 = 成交量 / 客资量
     * */
    private Double successRate;
    /**
     * 有效客资成交率 = 成交量 / 有效量
     * */
    private Double validSuccessRate;
    /**
     * 入店成交率 = 成交量 / 入店量
     * */
    private Double comeShopSuccessRate;
    /**
     * 花费：花费记录在各个来源上面，做累加计算
     * */
    private String sourceClientCost;
    /**
     * 毛客资成本：花费 / 客资量
     * */
    private String clientCost;
    /**
     * 有效客资成本：花费 / 有效量
     * */
    private String validClientCost;
    /**
     * 入店成本：花费 / 入店量
     * */
    private String appointClientCost;
    /**
     * 成交成本：花费 / 成交量
     * */
    private String successClientCost;
    /**
     * 套系总金额：成交客资里面的 sum(AMOUNT)
     * */
    private int successClientAmount;
    /**
     * 总已收金额：成交客资和保留成交客资里面的 sum(STAYAMOUT)
     * */
    private int successClientStayAmount;
    /**
     * 已收占比：总已收 / 套系总金额
     * */
    private Double receiveRatio;
    /**
     * ROI
     * */
    private String roi;
    /**
     * 成交均价
     * */
    private String clientAvgAmount;
    /**
     * 已加微信
     * */
    private int successWeChat;
    /**
     * 未加微信
     * */
    private int failWeChat;
    /**
     * 待通过微信
     * */
    private int waitWeChat;

    /**
     * 筛选中
     * */
    private int screenWaitClientCount;

    /**
     * 筛选待定
     * */
    private int undeterWaitClientCount;

    /**
     * 筛选无效
     * */
    private int invalidWaitClientCount;

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

    public int getAppointClientCountNotContains() {
        return appointClientCountNotContains;
    }

    public void setAppointClientCountNotContains(int appointClientCountNotContains) {
        this.appointClientCountNotContains = appointClientCountNotContains;
    }

    public int getAppointClientCountNotContainsAndCreateTime() {
        return appointClientCountNotContainsAndCreateTime;
    }

    public void setAppointClientCountNotContainsAndCreateTime(int appointClientCountNotContainsAndCreateTime) {
        this.appointClientCountNotContainsAndCreateTime = appointClientCountNotContainsAndCreateTime;
    }



    public int getScreenWaitClientCount() {
        return screenWaitClientCount;
    }

    public void setScreenWaitClientCount(int screenWaitClientCount) {
        this.screenWaitClientCount = screenWaitClientCount;
    }

    public int getUndeterWaitClientCount() {
        return undeterWaitClientCount;
    }

    public void setUndeterWaitClientCount(int undeterWaitClientCount) {
        this.undeterWaitClientCount = undeterWaitClientCount;
    }

    public int getInvalidWaitClientCount() {
        return invalidWaitClientCount;
    }

    public void setInvalidWaitClientCount(int invalidWaitClientCount) {
        this.invalidWaitClientCount = invalidWaitClientCount;
    }

    public int getSpecifiedTimeClientCount() {
        return specifiedTimeClientCount;
    }

    public void setSpecifiedTimeClientCount(int specifiedTimeClientCount) {
        this.specifiedTimeClientCount = specifiedTimeClientCount;
    }

    public int getSuccessWeChat() {
        return successWeChat;
    }

    public void setSuccessWeChat(int successWeChat) {
        this.successWeChat = successWeChat;
    }

    public int getFailWeChat() {
        return failWeChat;
    }

    public void setFailWeChat(int failWeChat) {
        this.failWeChat = failWeChat;
    }

    public int getWaitWeChat() {
        return waitWeChat;
    }

    public void setWaitWeChat(int waitWeChat) {
        this.waitWeChat = waitWeChat;
    }

    public int getUnAllClientCount() {
        return unAllClientCount;
    }

    public void setUnAllClientCount(int unAllClientCount) {
        this.unAllClientCount = unAllClientCount;
    }


    public String getClientAvgAmount() {
        return clientAvgAmount;
    }

    public void setClientAvgAmount(String clientAvgAmount) {
        this.clientAvgAmount = clientAvgAmount;
    }

    public Double getWaitRate() {
        return waitRate;
    }

    public void setWaitRate(Double waitRate) {
        this.waitRate = waitRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public int getAllClientCount() {
        return allClientCount;
    }

    public void setAllClientCount(int allClientCount) {
        this.allClientCount = allClientCount;
    }

    public int getValidClientCount() {
        return validClientCount;
    }

    public void setValidClientCount(int validClientCount) {
        this.validClientCount = validClientCount;
    }

    public int getWaitClientCount() {
        return waitClientCount;
    }

    public void setWaitClientCount(int waitClientCount) {
        this.waitClientCount = waitClientCount;
    }

    public int getInValidClientCount() {
        return inValidClientCount;
    }

    public void setInValidClientCount(int inValidClientCount) {
        this.inValidClientCount = inValidClientCount;
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

    public Double getAppointRate() {
        return appointRate;
    }

    public void setAppointRate(Double appointRate) {
        this.appointRate = appointRate;
    }

    public Double getValidAppointRate() {
        return validAppointRate;
    }

    public void setValidAppointRate(Double validAppointRate) {
        this.validAppointRate = validAppointRate;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Double getValidSuccessRate() {
        return validSuccessRate;
    }

    public void setValidSuccessRate(Double validSuccessRate) {
        this.validSuccessRate = validSuccessRate;
    }

    public Double getComeShopSuccessRate() {
        return comeShopSuccessRate;
    }

    public void setComeShopSuccessRate(Double comeShopSuccessRate) {
        this.comeShopSuccessRate = comeShopSuccessRate;
    }

    public String getSourceClientCost() {
        return sourceClientCost;
    }

    public void setSourceClientCost(String sourceClientCost) {
        this.sourceClientCost = sourceClientCost;
    }

    public String getClientCost() {
        return clientCost;
    }

    public void setClientCost(String clientCost) {
        this.clientCost = clientCost;
    }

    public String getValidClientCost() {
        return validClientCost;
    }

    public void setValidClientCost(String validClientCost) {
        this.validClientCost = validClientCost;
    }

    public String getAppointClientCost() {
        return appointClientCost;
    }

    public void setAppointClientCost(String appointClientCost) {
        this.appointClientCost = appointClientCost;
    }

    public String getSuccessClientCost() {
        return successClientCost;
    }

    public void setSuccessClientCost(String successClientCost) {
        this.successClientCost = successClientCost;
    }

    public int getSuccessClientAmount() {
        return successClientAmount;
    }

    public void setSuccessClientAmount(int successClientAmount) {
        this.successClientAmount = successClientAmount;
    }

    public int getSuccessClientStayAmount() {
        return successClientStayAmount;
    }

    public void setSuccessClientStayAmount(int successClientStayAmount) {
        this.successClientStayAmount = successClientStayAmount;
    }

    public Double getReceiveRatio() {
        return receiveRatio;
    }

    public void setReceiveRatio(Double receiveRatio) {
        this.receiveRatio = receiveRatio;
    }

    public String getRoi() {
        return roi;
    }

    public void setRoi(String roi) {
        this.roi = roi;
    }
}
