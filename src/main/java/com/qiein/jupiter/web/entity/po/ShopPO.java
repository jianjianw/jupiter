package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 拍摄地（门店）
 */
public class ShopPO extends BaseEntity {

    /**
     * 拍摄地名称
     */
    private String shopName;
    /**
     * 类型；1：可接单门店；2：拍摄基地；3：外展
     */
    private int type;
    /**
     * 是否开启
     */
    private boolean showFlag;
    /**
     * 优先级
     */
    private int priority;
    /**
     * 地址
     */
    private String address;
    /**
     * 客服电话
     */
    private String servicePhone;
    /**
     * 企业ID
     */
    private int companyId;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}