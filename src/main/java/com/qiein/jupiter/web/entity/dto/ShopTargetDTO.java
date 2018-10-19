package com.qiein.jupiter.web.entity.dto;

public class ShopTargetDTO {
    /**
     * 门店id
     */
    private int shopId;
    /**
     * 公司id
     */
    private int companyId;
    /**
     * 目标时间
     */
    private int time;
    /**
     * 总成交量目标
     */
    private String totalSuccessCountTarget;
    /**
     * 门店指名客资成交量目标
     */
    private String shopCallOnSuccessCountTarget;
    /**
     * 门店指名有效客资量目标
     */
    private String shopCallOnValidCountTarget;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getTotalSuccessCountTarget() {
        return totalSuccessCountTarget;
    }

    public void setTotalSuccessCountTarget(String totalSuccessCountTarget) {
        this.totalSuccessCountTarget = totalSuccessCountTarget;
    }

    public String getShopCallOnSuccessCountTarget() {
        return shopCallOnSuccessCountTarget;
    }

    public void setShopCallOnSuccessCountTarget(String shopCallOnSuccessCountTarget) {
        this.shopCallOnSuccessCountTarget = shopCallOnSuccessCountTarget;
    }

    public String getShopCallOnValidCountTarget() {
        return shopCallOnValidCountTarget;
    }

    public void setShopCallOnValidCountTarget(String shopCallOnValidCountTarget) {
        this.shopCallOnValidCountTarget = shopCallOnValidCountTarget;
    }
}
