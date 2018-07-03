package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

import java.util.List;

/**
 * 拍摄地（门店）
 */
public class ShopVO extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 拍摄地名称
     */
    private String shopName;

    /**
     * 是否开启
     */
    private boolean showFlag;

    /**
     * 排序
     */
    private int priority;

    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 门市人员集合
     */
    private List<BaseStaffVO> staffVOList;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<BaseStaffVO> getStaffVOList() {
        return staffVOList;
    }

    public void setStaffVOList(List<BaseStaffVO> staffVOList) {
        this.staffVOList = staffVOList;
    }
}
