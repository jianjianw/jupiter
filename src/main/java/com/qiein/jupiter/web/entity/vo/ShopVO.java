package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 拍摄地（门店）
 */
public class ShopVO extends BaseEntity {

    /**
     * 拍摄地名称
     */
    private String shopName;

    /**
     * 是否开启
     */
    private boolean showFlag;

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

    public boolean isShowFlag() {
        return showFlag;
    }

    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
