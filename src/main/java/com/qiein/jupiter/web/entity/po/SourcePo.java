package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 来源数据库对象
 */
public class SourcePo extends BaseEntity {

    /**
     * 来源编号
     */
    private Integer srcId;

    /**
     * 来源名称
     */
    private String srcName;

    /**
     * 来源图片地址
     */
    private String srcImg;

    /**
     * 来源类型：
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     */
    private Integer typeId;

    /**
     * 来源所属渠道编号
     */
    private Integer grpId;

    /**
     * 来源所属渠道名称
     */
    private String srcGrpName;

    /**
     * 来源所属品牌编号
     */
    private Integer brandId;

    /**
     * 来源所属品牌名称
     */
    private String brandName;

    /**
     * 所属公司编号
     */
    private Integer companyId;

    /**
     *  排序优先级
     */
    private Integer priority;

    /**
     * 是否开启筛选
     */
    private Boolean isFilter;

    /**
     * 是否启用
     */
    private Boolean isShow;

    /**
     * 无参构造
     */
    public SourcePo() {

    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getGrpId() {
        return grpId;
    }

    public void setGrpId(Integer grpId) {
        this.grpId = grpId;
    }

    public String getSrcGrpName() {
        return srcGrpName;
    }

    public void setSrcGrpName(String srcGrpName) {
        this.srcGrpName = srcGrpName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

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

    public Boolean getFilter() {
        return isFilter;
    }

    public void setFilter(Boolean filter) {
        isFilter = filter;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public Integer getPriority() { return priority; }

    public void setPriority(Integer priority) { this.priority = priority; }
}
