package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * Created by Administrator on 2018/4/24 0024.
 */
public class BrandPO extends BaseEntity {

    /**
     * 品牌名
     */
    @NotEmptyStr
    private String brandName;

    /**
     * 所属公司编号
     */
    private Integer companyId;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}