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
