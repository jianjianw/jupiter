package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 收款记录
 * @author gaoxiaoli 2018/7/17
 */

public class CashLogPO extends BaseEntity {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 客资ID
     */
    @NotEmptyStr(message = "{cashlog.kzId.null}")
    private String kzId;
    /**
     * 支付方式
     */
    @Id(message = "{cashlog.payStyle.null}")
    private int payStyle;
    /**
     * 收款金额
     */
    @Id(message = "{cashlog.amount.null}")
    private int amount;
    /**
     * 收款人ID
     */
    @Id(message = "{cashlog.staffId.null}")
    private int staffId;
    /**
     * 收款人姓名
     */
    @NotEmptyStr(message = "{cashlog.staffName.null}")
    private String staffName;
    /**
     * 收款时间
     */
    @Id(message = "{cashlog.paymentTime.null}")
    private int paymentTime;
    /**
     * 操作人ID
     */
    private int operaId;
    /**
     *操作人姓名
     */
    private String operaName;
    /**
     * 创建时间
     */
    private int createTime;
    /**
     * 企业ID
     */
    private int companyId;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(int paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getOperaId() {
        return operaId;
    }

    public void setOperaId(int operaId) {
        this.operaId = operaId;
    }

    public String getOperaName() {
        return operaName;
    }

    public void setOperaName(String operaName) {
        this.operaName = operaName;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
