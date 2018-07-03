package com.qiein.jupiter.web.entity.vo;

/**
 * FileName: ReportsConditionVO
 *
 * @author: yyx
 * @Date: 2018-6-30 18:06
 */
public class ReportsConditionVO {
    /**
     * 开始时间
     * */
    private Integer start;
    /**
     * 结束时间
     * */
    private Integer end;
    /**
     * 照片类型
     * */
    private String typeId;
    /**
     * 来源id
     * */
    private String sourceId;
    /**
     * 客资联系方式类型
     * */
    private Integer phoneLimit;
    /**
     * 录取类型
     * */
    private Integer sparelimit;

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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getPhoneLimit() {
        return phoneLimit;
    }

    public void setPhoneLimit(Integer phoneLimit) {
        this.phoneLimit = phoneLimit;
    }

    public Integer getSparelimit() {
        return sparelimit;
    }

    public void setSparelimit(Integer sparelimit) {
        this.sparelimit = sparelimit;
    }
}
