package com.qiein.jupiter.web.entity.vo;

public class ZjsKzOfMonthOutVO {
    /**
     *  日期键值
     */
    private String dayKey;
    /**
     *  日期数值
     */
    private String day;

    /**
     * 渠道名称
     */
    private String srcName;

    /**
     * 渠道图片
     */
    private String srcImg;
    /**
     * 渠道id
     */
    private Integer srcId;
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
     * 花费
     * */
    private String allCost;

    /**
     * 毛客资成本
     * */
    private String clientCost;

    /**
     * 有效客资成本
     * */
    private String validClientCost;

    /**
     * 入店成本
     * */
    private String comeShopClientCost;

    /**
     * 成交成本
     * */
    private String successClientCost;

    /**
     * 成交均价
     */
    private double avgAmount;

    /**
     * 营业额
     */
    private double amount;

    /**
     * ROI
     * */
    private String ROI;

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

    public String getAllCost() {
        return allCost;
    }

    public void setAllCost(String allCost) {
        this.allCost = allCost;
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

    public String getComeShopClientCost() {
        return comeShopClientCost;
    }

    public void setComeShopClientCost(String comeShopClientCost) {
        this.comeShopClientCost = comeShopClientCost;
    }

    public String getSuccessClientCost() {
        return successClientCost;
    }

    public void setSuccessClientCost(String successClientCost) {
        this.successClientCost = successClientCost;
    }

    public String getROI() {
        return ROI;
    }

    public void setROI(String ROI) {
        this.ROI = ROI;
    }

    public String getDayKey() {
        return dayKey;
    }

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public int getFilterPendingClientCount() {
        return filterPendingClientCount;
    }

    public void setFilterPendingClientCount(int filterPendingClientCount) {
        this.filterPendingClientCount = filterPendingClientCount;
    }

    public int getFilterInValidClientCount() {
        return filterInValidClientCount;
    }

    public void setFilterInValidClientCount(int filterInValidClientCount) {
        this.filterInValidClientCount = filterInValidClientCount;
    }

    public int getSuccessClientCount() {
        return successClientCount;
    }

    public void setSuccessClientCount(int successClientCount) {
        this.successClientCount = successClientCount;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
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

    public int getFilterInClientCount() {
        return filterInClientCount;
    }

    public void setFilterInClientCount(int filterInClientCount) {
        this.filterInClientCount = filterInClientCount;
    }

    public int getComeShopClientCount() {
        return comeShopClientCount;
    }

    public void setComeShopClientCount(int comeShopClientCount) {
        this.comeShopClientCount = comeShopClientCount;
    }
}
