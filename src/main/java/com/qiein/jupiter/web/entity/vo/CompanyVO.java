package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

import java.util.List;

public class CompanyVO extends BaseEntity {

    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司类型
     */
    private int typeId;
    /**
     * logo
     */
    private String logo;
    /**
     * 企业左上角菜单栏
     */
    private List<MenuVO> menuList;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<MenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
    }
}
