package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 拍摄地 字典
 *
 * @Author: shiTao
 */
public class ShopDictVO implements Serializable {

    private static final long serialVersionUID = -5927333274618120861L;

    private int id;
    /**
     * 拍摄地名称
     */
    private String shopName;
    /**
     * 是否显示
     */
    private boolean showFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
