package com.qiein.jupiter.web.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 客资导出封装类
 * gaoxiaoli
 * 2018/5/24
 */

public class ClientExportVO implements Serializable {
    private static final long serialVersionUID = 1288139895464503208L;
    /**
     * 客资编号
     */
    @Excel(name = "编号", width = 10)
    private int id;
    /**
     * 客服组名称
     */
    @Excel(name = "组别", width = 10)
    private String groupName;
    /**
     * 录入时间接收格式
     */
    @Excel(name = "录入时间", width = 20)
    private String createTime;

    /**
     * 推广员姓名
     */
    @Excel(name = "推广", width = 20)
    private String collectorName;
    /**
     * 客资电话
     */
    @Excel(name = "手机号", width = 20)
    private String kzPhone;
    /**
     * 客资微信
     */
    @Excel(name = "微信号", width = 20)
    private String kzWechat;
    /**
     * 客资QQ
     */
    @Excel(name = "QQ", width = 20)
    private String kzQq;
    /**
     * 客资旺旺
     */
    @Excel(name = "旺旺", width = 20)
    private String kzWw;

    /**
     * 拍摄地名称
     */
    @Excel(name = "拍摄地", width = 10)
    private String shopName;
    /**
     * 邀约客服姓名
     */
    @Excel(name = "客服", width = 20)
    private String appointName;
    /**
     * 渠道名称
     */
    @Excel(name = "来源", width = 10)
    private String sourceName;
    /**
     * 状态名称
     */
    @Excel(name = "状态", width = 15)
    private String statusName;
    /**
     * 渠道名
     */
    @Excel(name = "渠道", width = 10)
    private String channelName;
    /**
     * 推广备注
     */
    @Excel(name = "推广备注", width = 50)
    private String remark;
    /**
     * 省份地址
     */
    @Excel(name = "省份", width = 20)
    private String address;
    /**
     * 关键词
     */
    @Excel(name = "关键字", width = 20)
    private String keyWord;
    /**
     * 无效原因
     */
    @Excel(name = "无效原因", width = 30)
    private String invalidLabel;
    /**
     * 销售备注
     */
    @Excel(name = "销售备注", width = 40)
    private String memo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getKzPhone() {
        return kzPhone;
    }

    public void setKzPhone(String kzPhone) {
        this.kzPhone = kzPhone;
    }

    public String getKzWechat() {
        return kzWechat;
    }

    public void setKzWechat(String kzWechat) {
        this.kzWechat = kzWechat;
    }

    public String getKzQq() {
        return kzQq;
    }

    public void setKzQq(String kzQq) {
        this.kzQq = kzQq;
    }

    public String getKzWw() {
        return kzWw;
    }

    public void setKzWw(String kzWw) {
        this.kzWw = kzWw;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getInvalidLabel() {
        return invalidLabel;
    }

    public void setInvalidLabel(String invalidLabel) {
        this.invalidLabel = invalidLabel;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
