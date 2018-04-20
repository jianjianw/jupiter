package com.qiein.jupiter.enums;

public enum ShopTypeEnum {
    SHOP(1, "可接单门店"),
    SHOOTING(2, "拍摄基地"),
    OUT(3, "外展");

    private int shopType;
    private String shopTypeDesc;

    ShopTypeEnum(int shopType, String shopTypeDesc) {
        this.shopType = shopType;
        this.shopTypeDesc = shopTypeDesc;
    }

    public int getShopType() {
        return shopType;
    }


    public String getShopTypeDesc() {
        return shopTypeDesc;
    }


}
