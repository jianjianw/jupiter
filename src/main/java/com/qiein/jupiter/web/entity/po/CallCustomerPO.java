package com.qiein.jupiter.web.entity.po;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public class CallCustomerPO {
    /**
     * id
     * */
    private Integer id;

    /**
     * 员工id
     * */
    private Integer staffId;

    /**
     * 公司id
     * */
    private Integer companyId;

    /**
     * 实例id
     * */
    private Integer callId;

    /**
     * 电话
     * */
    private String phone;

    /**
     * 创建时间
     * */
    private Integer createTime;

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
