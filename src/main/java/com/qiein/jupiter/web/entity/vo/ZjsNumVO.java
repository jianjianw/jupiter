package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 转介绍报表，，统计每列字段
 *
 * @author gaoxiaoli 2018/7/5
 */

public class ZjsNumVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int staffId;//员工ID
    private String nickName;//员工昵称
    private int sourceId;//来源ID
    private String groupId;//小组ID
    private int numAll;// 总客资量
    private int kzNum;// 客资量---毛客资咨询量
    private int yxNum;// 有效量---有效客资咨询量
    private int wxNum;// 无效量
    private int ddNum;//待定量
    private int rdNum;// 入店量
    private int cjNum;// 成交量
    private int rdCjNum;//入店成交量
    private int zxCjNum;//在线成交量
    private String yxRate;// 有效率
    private String grossRdRate;// 毛客资入店率
    private String rdRate; // 有效客资入店率
    private String cjRate; // 成交率--入店成交率
    private String grossCjRate;// 毛客资成交率
    private String yxCjRate;// 有效客资成交率
    private double jjAmount;// 均价
    private double yyAmount;// 营业额
    private double haveAmount;// 已收金额
    private int addWechatNum;// 已加微信个数
    private int noWechatNum;// 未加微信个数
    private int bePassWechatNum;// 待通过微信个数
    private int weekEndRdNum;//周末入店量
    private int notWeekEndRdNum;//非周末入店量
    private double realAmount;//实收金额
    private double bkAmount;//实收金额
    private String ddRate;//待定率
    private String wxRate;//无效率

    public String getDdRate() {
        return ddRate;
    }

    public void setDdRate(String ddRate) {
        this.ddRate = ddRate;
    }

    public String getWxRate() {
        return wxRate;
    }

    public void setWxRate(String wxRate) {
        this.wxRate = wxRate;
    }

    public ZjsNumVO() {
        this.yxRate = "0.00";
        this.grossRdRate = "0.00";
        this.rdRate = "0.00";
        this.cjRate = "0.00";
        this.grossCjRate = "0.00";
        this.yxCjRate = "0.00";
        this.wxRate="0.00";
        this.ddRate="0.00";
        this.jjAmount = 0.00;
        this.yyAmount = 0.00;
        this.haveAmount = 0.0;
    }

    public double getBkAmount() {
        return bkAmount;
    }

    public void setBkAmount(double bkAmount) {
        this.bkAmount = bkAmount;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public ZjsNumVO(String type) {
        if ("total".equalsIgnoreCase(type)) {
            this.sourceId = 0;
            this.staffId = 0;
            this.groupId = "0";
            this.yxRate = "0.00";
            this.grossRdRate = "0.00";
            this.rdRate = "0.00";
            this.cjRate = "0.00";
            this.grossCjRate = "0.00";
            this.yxCjRate = "0.00";
            this.wxRate="0.00";
            this.ddRate="0.00";
            this.jjAmount = 0.00;
            this.yyAmount = 0.00;
            this.haveAmount = 0.0;
        }
    }

    public int getRdCjNum() {
        return rdCjNum;
    }

    public void setRdCjNum(int rdCjNum) {
        this.rdCjNum = rdCjNum;
    }

    public int getZxCjNum() {
        return zxCjNum;
    }

    public void setZxCjNum(int zxCjNum) {
        this.zxCjNum = zxCjNum;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getNumAll() {
        return numAll;
    }

    public ZjsNumVO setNumAll(int numAll) {
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

    public String getYxRate() {
        return yxRate;
    }

    public void setYxRate(String yxRate) {
        this.yxRate = yxRate;
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

    public double getJjAmount() {
        return jjAmount;
    }

    public void setJjAmount(double jjAmount) {
        this.jjAmount = jjAmount;
    }

    public double getYyAmount() {
        return yyAmount;
    }

    public void setYyAmount(double yyAmount) {
        this.yyAmount = yyAmount;
    }

    public double getHaveAmount() {
        return haveAmount;
    }

    public void setHaveAmount(double haveAmount) {
        this.haveAmount = haveAmount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public int getAddWechatNum() {
        return addWechatNum;
    }

    public void setAddWechatNum(int addWechatNum) {
        this.addWechatNum = addWechatNum;
    }

    public int getNoWechatNum() {
        return noWechatNum;
    }

    public void setNoWechatNum(int noWechatNum) {
        this.noWechatNum = noWechatNum;
    }

    public int getBePassWechatNum() {
        return bePassWechatNum;
    }

    public void setBePassWechatNum(int bePassWechatNum) {
        this.bePassWechatNum = bePassWechatNum;
    }

    public int getWeekEndRdNum() {
        return weekEndRdNum;
    }

    public void setWeekEndRdNum(int weekEndRdNum) {
        this.weekEndRdNum = weekEndRdNum;
    }

    public int getNotWeekEndRdNum() {
        return notWeekEndRdNum;
    }

    public void setNotWeekEndRdNum(int notWeekEndRdNum) {
        this.notWeekEndRdNum = notWeekEndRdNum;
    }
}
