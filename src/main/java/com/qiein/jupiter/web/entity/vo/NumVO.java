package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 统计每列字段
 *
 * @author gaoxiaoli 2018/7/5
 */

public class NumVO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int channelId;// 渠道ID
    private int numAll;// 总客资量
    private int kzNum;// 客资量---毛客资咨询量
    private int yxNum;// 有效量---有效客资咨询量
    private int wxNum;// 无效量
    private int ddNum;// 待定量
    private int rdNum;// 入店量
    private int cjNum;// 成交量
    private String yxRate;// 有效率
    private String wxRate;// 无效率
    private String ddRate;// 待定率
    private String grossRdRate;// 毛客资入店率
    private String rdRate; // 有效客资入店率
    private String cjRate; // 成交率--入店成交率
    private String grossCjRate;// 毛客资成交率
    private String yxCjRate;// 有效客资成交率
    private double amount;// 花费
    private double grossCost;// 毛客资成本
    private double yxCost;// 有效客资成本
    private double rdCost;// 入店成本
    private double cjCost;// 成交成本
    private double jjAmount;// 均价
    private double yyAmount;// 营业额
    private double haveAmount;// 已收金额
    private String roi;// ROI
    private int msNum;//本月录入，本月成交的客资量

    public NumVO() {
        this.yxRate = "0.00";
        this.wxRate = "0.00";
        this.ddRate = "0.00";
        this.grossRdRate = "0.00";
        this.rdRate = "0.00";
        this.cjRate = "0.00";
        this.grossCjRate = "0.00";
        this.yxCjRate = "0.00";
        this.amount = 0.00;
        this.jjAmount = 0.00;
        this.grossCost = 0.00;
        this.yxCost = 0.00;
        this.rdCost = 0.00;
        this.cjCost = 0.00;
        this.yyAmount = 0.00;
        this.roi = "0.0";
        this.haveAmount = 0.0;
    }

    public NumVO(String type) {
        if ("total".equalsIgnoreCase(type)) {
            this.channelId = -1;
            this.yxRate = "0.00";
            this.wxRate = "0.00";
            this.ddRate = "0.00";
            this.grossRdRate = "0.00";
            this.rdRate = "0.00";
            this.cjRate = "0.00";
            this.grossCjRate = "0.00";
            this.yxCjRate = "0.00";
            this.amount = 0.00;
            this.jjAmount = 0.00;
            this.grossCost = 0.00;
            this.yxCost = 0.00;
            this.rdCost = 0.00;
            this.cjCost = 0.00;
            this.yyAmount = 0.00;
            this.roi = "0.0";
            this.haveAmount = 0.0;
        }
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getNumAll() {
        return numAll;
    }

    public NumVO setNumAll(int numAll) {
        this.numAll = numAll;
        return this;
    }

    public int getKzNum() {
        return kzNum;
    }

    public void setKzNum(int kzNum) {
        this.kzNum = kzNum;
    }

    public int getYxNum() {
        return yxNum;
    }

    public void setYxNum(int yxNum) {
        this.yxNum = yxNum;
    }

    public int getWxNum() {
        return wxNum;
    }

    public void setWxNum(int wxNum) {
        this.wxNum = wxNum;
    }

    public int getDdNum() {
        return ddNum;
    }

    public void setDdNum(int ddNum) {
        this.ddNum = ddNum;
    }

    public int getRdNum() {
        return rdNum;
    }

    public void setRdNum(int rdNum) {
        this.rdNum = rdNum;
    }

    public int getCjNum() {
        return cjNum;
    }

    public void setCjNum(int cjNum) {
        this.cjNum = cjNum;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getGrossCost() {
        return grossCost;
    }

    public void setGrossCost(Double grossCost) {
        this.grossCost = grossCost;
    }

    public Double getYxCost() {
        return yxCost;
    }

    public void setYxCost(Double yxCost) {
        this.yxCost = yxCost;
    }

    public Double getRdCost() {
        return rdCost;
    }

    public void setRdCost(Double rdCost) {
        this.rdCost = rdCost;
    }

    public Double getCjCost() {
        return cjCost;
    }

    public void setCjCost(Double cjCost) {
        this.cjCost = cjCost;
    }

    public Double getJjAmount() {
        return jjAmount;
    }

    public void setJjAmount(Double jjAmount) {
        this.jjAmount = jjAmount;
    }

    public Double getYyAmount() {
        return yyAmount;
    }

    public void setYyAmount(Double yyAmount) {
        this.yyAmount = yyAmount;
    }

    public Double getHaveAmount() {
        return haveAmount;
    }

    public void setHaveAmount(Double haveAmount) {
        this.haveAmount = haveAmount;
    }

    public String getYxRate() {
        return yxRate;
    }

    public void setYxRate(String yxRate) {
        this.yxRate = yxRate;
    }

    public String getWxRate() {
        return wxRate;
    }

    public void setWxRate(String wxRate) {
        this.wxRate = wxRate;
    }

    public String getDdRate() {
        return ddRate;
    }

    public void setDdRate(String ddRate) {
        this.ddRate = ddRate;
    }

    public String getGrossRdRate() {
        return grossRdRate;
    }

    public void setGrossRdRate(String grossRdRate) {
        this.grossRdRate = grossRdRate;
    }

    public String getRdRate() {
        return rdRate;
    }

    public void setRdRate(String rdRate) {
        this.rdRate = rdRate;
    }

    public String getCjRate() {
        return cjRate;
    }

    public void setCjRate(String cjRate) {
        this.cjRate = cjRate;
    }

    public String getGrossCjRate() {
        return grossCjRate;
    }

    public void setGrossCjRate(String grossCjRate) {
        this.grossCjRate = grossCjRate;
    }

    public String getYxCjRate() {
        return yxCjRate;
    }

    public void setYxCjRate(String yxCjRate) {
        this.yxCjRate = yxCjRate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setGrossCost(double grossCost) {
        this.grossCost = grossCost;
    }

    public void setYxCost(double yxCost) {
        this.yxCost = yxCost;
    }

    public void setRdCost(double rdCost) {
        this.rdCost = rdCost;
    }

    public void setCjCost(double cjCost) {
        this.cjCost = cjCost;
    }

    public void setJjAmount(double jjAmount) {
        this.jjAmount = jjAmount;
    }

    public void setYyAmount(double yyAmount) {
        this.yyAmount = yyAmount;
    }

    public void setHaveAmount(double haveAmount) {
        this.haveAmount = haveAmount;
    }

    public String getRoi() {
        return roi;
    }

    public void setRoi(String roi) {
        this.roi = roi;
    }

    public int getMsNum() {
        return msNum;
    }

    public void setMsNum(int msNum) {
        this.msNum = msNum;
    }
}
