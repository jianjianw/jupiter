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
    /**
     * 总拍摄目标
     */
    private String totalShootingTarget;
    /**
     * 有效客资量目标
     */
    private String validCountTarget;
    /**
     * 总拍摄量
     */
    private String totalShooting;


    /**
     * 类型
     */
    private int type;

    public String getTotalShootingTarget() {
        return totalShootingTarget;
    }

    public void setTotalShootingTarget(String totalShootingTarget) {
        this.totalShootingTarget = totalShootingTarget;
    }

    public String getValidCountTarget() {
        return validCountTarget;
    }

    public void setValidCountTarget(String validCountTarget) {
        this.validCountTarget = validCountTarget;
    }

    public String getTotalShooting() {
        return totalShooting;
    }

    public void setTotalShooting(String totalShooting) {
        this.totalShooting = totalShooting;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
