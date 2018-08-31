package com.qiein.jupiter.web.entity.dto;

/**
 * 报表简单参数接收类
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/29 16:40
 */
public class ReportParamDTO {
    private Integer start;
    private Integer end;
    private Integer staffId;
    private Integer companyId;
    /**
     * 报表类型，ds 为电商报表，zjs为转介绍报表
     */
    private String type;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
