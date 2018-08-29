package com.qiein.jupiter.web.entity.po;

import java.math.BigDecimal;

public class ForDayPO {
    private Integer day;
    private String forDay;
    private String srcId;
    private BigDecimal rate;
    private Integer companyId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getForDay() {
        return forDay;
    }

    public void setForDay(String forDay) {
        this.forDay = forDay;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
