package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 成功订单的消息
 */
public class OrderSuccessMsg implements Serializable{

    private static final long serialVersionUID = -7734968987602931991L;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 公司id
     */
    private int companyId;
    /**
     * 员工头像
     */
    private String headImg;
    /**
     * 背景图像
     */
    private String srcImg;
    /**
     * 门店名称
     */
    private String shopName;
    /**
     * 订单金额
     */
    private String amount;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
