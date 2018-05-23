package com.qiein.jupiter.enums;

/**
 * 公司类型
 *
 * @Author: shiTao
 */
public enum CompanyEnum {
    //婚纱
    WeddingDress(1, "婚纱"),
    //儿童
    Children(2, "儿童"),
    //婚庆
    WeddingCeremony(3, "婚庆"),
    //礼服
    FullDress(4, "礼服"),
    //教育
    Education(5, "教育"),
    //旅拍
    TravelShot(6, "旅拍"),
    //写真
    Portrait(7, "写真");

    private int id;

    private String desc;

    CompanyEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
