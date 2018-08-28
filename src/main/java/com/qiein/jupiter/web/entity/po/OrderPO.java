package com.qiein.jupiter.web.entity.po;


import java.math.BigDecimal;

/**
 * FileName: OrderPO
 *
 * @author: yyx
 * @Date: 2018-7-14 10:51
 */
public class OrderPO  {
    /**
     * id
     * */
    private Integer id;

    /**
     * 公司id
     * */
    private Integer companyId;

    /**
     * 订单名称
     * */
    private String name;

    /**
     * 订单类型
     * */
    private Integer type;

    /**
     * 订单流水号
     * */
    private String code;

    /**
     * 交易流水号
     * */
    private String batchCode;

    /**
     * 付款金额
     * */
    private BigDecimal payment;

    /**
     * 付款方式
     * */
    private Integer paymentType;

    /**
     * 订单状态
     * */
    private Integer status;

    /**
     * 备注
     * */
    private String remark;

    /**
     * 付款时间
     * */
    private Integer paymentTime;

    /**
     * 创建时间
     * */
    private Integer createTime;

    /**
     * 更新时间
     * */
    private Integer updateTime;

    /**
     * 员工id
     * */
    private Integer staffId;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Integer paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}

