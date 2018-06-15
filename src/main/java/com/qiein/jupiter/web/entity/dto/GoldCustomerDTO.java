package com.qiein.jupiter.web.entity.dto;
/**
 * 金数据客资日志 入参
 * Author xiangliang
 */
public class GoldCustomerDTO {
    //开始时间
    private Integer startTime;
    //结束时间
    private Integer endTime;
    //表单id
    private String formId;
    //状态
    private String statusId;

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
