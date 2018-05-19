package com.qiein.jupiter.web.entity.vo;


import com.qiein.jupiter.web.entity.dto.PageDictDTO;
import com.qiein.jupiter.web.entity.po.PermissionPO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 员工基础信息
 */
public class StaffBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 9076352385052021856L;
    /**
     * 员工权限信息
     */
    private List<PermissionPO> permission;
    /**
     * 员工详细信息
     */
    private StaffDetailVO staffDetail;
    /**
     * 公司
     */
    private CompanyVO company;
    /**
     * 页面字典
     */
    private PageDictDTO pageDict;
    /**
     * 消息
     */
    private NewsTotalAmountAndFlag news;

    public NewsTotalAmountAndFlag getNews() {
        return news;
    }

    public void setNews(NewsTotalAmountAndFlag news) {
        this.news = news;
    }

    public PageDictDTO getPageDict() {
        return pageDict;
    }

    public void setPageDict(PageDictDTO pageDict) {
        this.pageDict = pageDict;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<PermissionPO> getPermission() {
        return permission;
    }

    public void setPermission(List<PermissionPO> permission) {
        this.permission = permission;
    }

    public StaffDetailVO getStaffDetail() {
        return staffDetail;
    }

    public void setStaffDetail(StaffDetailVO staffDetail) {
        this.staffDetail = staffDetail;
    }

    public CompanyVO getCompany() {
        return company;
    }

    public void setCompany(CompanyVO company) {
        this.company = company;
    }
}
