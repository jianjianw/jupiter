package com.qiein.jupiter.web.entity.dto;

/**
 * 公司转介绍设置DTO
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/7 13:12
 */
public class CompanyZjsSetDTO {
    private String qyZjsSet;
    private String lkZjsSet;
    private int companyId;

    public String getQyZjsSet() {
        return qyZjsSet;
    }

    public void setQyZjsSet(String qyZjsSet) {
        this.qyZjsSet = qyZjsSet;
    }

    public String getLkZjsSet() {
        return lkZjsSet;
    }

    public void setLkZjsSet(String lkZjsSet) {
        this.lkZjsSet = lkZjsSet;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
