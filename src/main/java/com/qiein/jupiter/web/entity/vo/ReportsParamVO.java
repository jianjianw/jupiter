package com.qiein.jupiter.web.entity.vo;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public class ReportsParamVO {
    /**
     * 开始时间
     */
    private Integer start;

    /**
     * 结束时间
     */
    private Integer end;

    /**
     * 拍摄类型
     */
    private String type;

    /**
     * 咨询方式code
     */
    private String zxStyleCode;

    /**
     * 公司id
     */
    private Integer companyId;
    /**
     * 小组ID
     */
    private String groupId;
    /**
     * 多个小组ID
     */
    private String groupIds;
    /**
     * 月份
     */
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public String getZxStyleCode() {
        return zxStyleCode;
    }

    public void setZxStyleCode(String zxStyleCode) {
        this.zxStyleCode = zxStyleCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
