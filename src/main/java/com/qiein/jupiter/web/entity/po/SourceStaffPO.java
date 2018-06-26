package com.qiein.jupiter.web.entity.po;

/**
 * FileName: SourceStaffPO
 *
 * @author: yyx
 * @Date: 2018-6-23 18:50
 */
public class SourceStaffPO {
    /**
     * id
     * */
    private Integer id;

    /**
     * 关联类型
     * */
    private Integer relaType;

    /**
     * 渠道id
     * */
    private Integer staffId;

    /**
     * 渠道id
     * */
    private Integer sourceId;

    /**
     * 公司id
     * */
    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRelaType() {
        return relaType;
    }

    public void setRelaType(Integer relaType) {
        this.relaType = relaType;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
