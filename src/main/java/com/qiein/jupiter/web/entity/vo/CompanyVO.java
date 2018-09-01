package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CompanyPO;

import java.util.List;

public class CompanyVO extends CompanyPO {

    private static final long serialVersionUID = 4068121917474419086L;
    /**
     * 企业左上角菜单栏
     */
    private List<MenuVO> menuList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<MenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
    }


}
