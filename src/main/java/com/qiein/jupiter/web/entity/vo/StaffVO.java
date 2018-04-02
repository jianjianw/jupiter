package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class StaffVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;// 主键ID
    private String name;// 职工姓名
    private String phone;// 职工电话
    private Integer companyId;// 所属企业ID
    private String headImg;// 头像
    private Boolean haveShow;// 是否显示

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getHaveShow() {
        return haveShow;
    }

    public void setHaveShow(Boolean haveShow) {
        this.haveShow = haveShow;
    }
}