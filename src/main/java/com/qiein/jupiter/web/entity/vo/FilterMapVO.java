package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.dto.PageFilterDTO;

import java.util.List;

/**
 * 可以筛选的表头
 */
public class FilterMapVO {
    /**
     * 品牌筛选
     */
    private List<PageFilterDTO> brand;
    /**
     * 小编筛选
     */
    private List<PageFilterDTO> collector;
    /**
     * 渠道筛选
     */
    private List<PageFilterDTO> channel;
    /**
     * 来源筛选
     */
    private List<PageFilterDTO> source;
    /**
     * 拍摄地
     */
    private List<PageFilterDTO> shop;
    /**
     * 状态
     */
    private List<PageFilterDTO> status;
    /**
     * 组别
     */
    private List<PageFilterDTO> dept;
    /**
     * 客服
     */
    private List<PageFilterDTO> appoint;
    /**
     * 意向
     */
    private List<PageFilterDTO> yxRank;
    /**
     * 婚期
     */
    private List<PageFilterDTO> marryRange;
    /**
     * 咨询方式
     */
    private List<PageFilterDTO> zxMode;
    /**
     * 咨询类型
     */
    private List<PageFilterDTO> zxType;
    /**
     * 预算范围
     */
    private List<PageFilterDTO> ysRange;
    /**
     * 无效原因
     */
    private List<PageFilterDTO> invalidReason;
    /**
     * 预排时间范围
     */
    private List<PageFilterDTO> ypRange;
    /**
     * 流失原因
     */
    private List<PageFilterDTO> runoffReason;

    public List<PageFilterDTO> getRunoffReason() {
        return runoffReason;
    }

    public void setRunoffReason(List<PageFilterDTO> runoffReason) {
        this.runoffReason = runoffReason;
    }

    public List<PageFilterDTO> getYpRange() {
        return ypRange;
    }

    public void setYpRange(List<PageFilterDTO> ypRange) {
        this.ypRange = ypRange;
    }

    public List<PageFilterDTO> getZxType() {
        return zxType;
    }

    public void setZxType(List<PageFilterDTO> zxType) {
        this.zxType = zxType;
    }

    public List<PageFilterDTO> getBrand() {
        return brand;
    }

    public void setBrand(List<PageFilterDTO> brand) {
        this.brand = brand;
    }

    public List<PageFilterDTO> getCollector() {
        return collector;
    }

    public void setCollector(List<PageFilterDTO> collector) {
        this.collector = collector;
    }

    public List<PageFilterDTO> getChannel() {
        return channel;
    }

    public void setChannel(List<PageFilterDTO> channel) {
        this.channel = channel;
    }

    public List<PageFilterDTO> getSource() {
        return source;
    }

    public void setSource(List<PageFilterDTO> source) {
        this.source = source;
    }

    public List<PageFilterDTO> getShop() {
        return shop;
    }

    public void setShop(List<PageFilterDTO> shop) {
        this.shop = shop;
    }

    public List<PageFilterDTO> getStatus() {
        return status;
    }

    public void setStatus(List<PageFilterDTO> status) {
        this.status = status;
    }

    public List<PageFilterDTO> getDept() {
        return dept;
    }

    public void setDept(List<PageFilterDTO> dept) {
        this.dept = dept;
    }

    public List<PageFilterDTO> getAppoint() {
        return appoint;
    }

    public void setAppoint(List<PageFilterDTO> appoint) {
        this.appoint = appoint;
    }

    public List<PageFilterDTO> getYxRank() {
        return yxRank;
    }

    public void setYxRank(List<PageFilterDTO> yxRank) {
        this.yxRank = yxRank;
    }

    public List<PageFilterDTO> getMarryRange() {
        return marryRange;
    }

    public void setMarryRange(List<PageFilterDTO> marryRange) {
        this.marryRange = marryRange;
    }

    public List<PageFilterDTO> getZxMode() {
        return zxMode;
    }

    public void setZxMode(List<PageFilterDTO> zxMode) {
        this.zxMode = zxMode;
    }

    public List<PageFilterDTO> getYsRange() {
        return ysRange;
    }

    public void setYsRange(List<PageFilterDTO> ysRange) {
        this.ysRange = ysRange;
    }

    public List<PageFilterDTO> getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(List<PageFilterDTO> invalidReason) {
        this.invalidReason = invalidReason;
    }
}
