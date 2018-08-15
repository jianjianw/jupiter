package com.qiein.jupiter.web.entity.vo;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public class ReportsParamVO {
    /**
     * 开始时间
     * */
    private Integer start;

    /**
     * 结束时间
     * */
    private Integer end;

    /**
     * 拍摄类型
     * */
    private Integer type;

    /**
     * groupid
     * */
    private String groupId;

    /**
     * 咨询方式code
     * */
    private String zxStyleCode;

    /**
     * 公司id
     * */
    private Integer companyId;


    public String getZxStyleCode() {
        return zxStyleCode;
    }

    public void setZxStyleCode(String zxStyleCode) {
        this.zxStyleCode = zxStyleCode;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
