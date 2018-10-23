package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

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
    @Excel(name = "编号", width = 10, height = 6)
    private String letterId;
    /**
     * 录入时间接收格式
     */
    @Excel(name = "录入时间", width = 20, height = 6)
    private String createTime;
    /**
     * 领取时间接收格式
     */
    @Excel(name = "领取时间", width = 20, height = 6)
    private String receiveTime;
    /**
     * 领取周期
     */
    @Excel(name = "领取周期", width = 10, height = 6)
    private String receivePeriod;
    /**
     * 预约时间
     */
    @Excel(name = "预约时间", width = 20, height = 6)
    private String appointTime;

    /**
     * 进店时间
     */
    @Excel(name = "进店时间", width = 10, height = 6)
    private String comeShopTime;

    /**
     * 拍摄类型
     */
    @Excel(name = "拍摄类型", width = 10, height = 6)
    private String shootType;

    /**
     * 门市
     */
    @Excel(name = "门市", width = 20, height = 6)
    private String receptorName;

    /**
     * 推广员姓名
     */
    @Excel(name = "推广", width = 20, height = 6)
    private String collectorName;
    /**
     * 客资姓名
     */
    @Excel(name = "姓名", width = 20, height = 6)
    private String kzName;
    /**
     * 性别
     */
    @Excel(name = "性别", width = 10, height = 6)
    private String kzSex;
    /**
     * 客资电话
     */
    @Excel(name = "手机号", width = 20, height = 6)
    private String kzPhone;
    /**
     * 客资微信
     */
    @Excel(name = "微信号", width = 20, height = 6)
    private String kzWechat;
    /**
     * 客资QQ
     */
    @Excel(name = "QQ", width = 20, height = 6)
    private String kzQq;
    /**
     * 客资旺旺
     */
    @Excel(name = "旺旺", width = 20, height = 6)
    private String kzWw;

    /**
     * 拍摄地名称
     */
    @Excel(name = "门店", width = 10, height = 6)
    private String shopName;
    /**
     * 渠道名
     */
    @Excel(name = "渠道", width = 10, height = 6)
    private String channelName;

    /**
     * 渠道名称
     */
    @Excel(name = "来源", width = 10, height = 6)
    private String sourceName;
    /**
     * 客服组名称
     */
    @Excel(name = "组别", width = 10, height = 6)
    private String groupName;
    /**
     * 邀约客服姓名
     */
    @Excel(name = "客服", width = 20, height = 6)
    private String appointName;

    @Excel(name = "咨询方式", width = 10, height = 6)
    private String zxStyle;
    /**
     * 意向等级
     */
    @Excel(name = "意向等级", width = 10, height = 6)
    private String yxLevel;
    /**
     * 套餐金额
     */
    @Excel(name = "套餐金额", width = 10, height = 6)
    private int amount;
    /**
     * 套餐金额
     */
    @Excel(name = "已收金额", width = 10, height = 6)
    private int stayAmount;
    /**
     * 套系名称
     */
    @Excel(name = "套系名称", width = 15, height = 6)
    private String packageName;

    /**
     * 订单时间
     */
    @Excel(name = "订单时间", width = 20, height = 6)
    private String successTime;
    /**
     * 订单周期
     */
    @Excel(name = "订单周期", width = 10, height = 6)
    private String successPeriod;

    /**
     * 状态名称
     */
    @Excel(name = "状态", width = 15, height = 6)
    private String statusName;

    /**
     * 推广备注
     */
    @Excel(name = "推广备注", width = 50, height = 6)
    private String remark;
    /**
     * 省份地址
     */
    @Excel(name = "省", width = 10, height = 6)
    private String province;
    /**
     * 市
     */
    @Excel(name = "市", width = 10, height = 6)
    private String city;
    /**
     * 关键词
     */
    @Excel(name = "关键字", width = 20, height = 6)
    private String keyWord;
    /**
     * 无效原因
     */
    @Excel(name = "无效原因", width = 30, height = 6)
    private String invalidLabel;
    /**
     * 无效备注
     */
    @Excel(name = "无效备注", width = 20, height = 6)
    private String invalidMemo;
    /**
     * 销售备注
     */
    @Excel(name = "备注", width = 40, height = 6)
    private String memo;
    /**
     * 广告id
     */
    @Excel(name = "广告id", width = 20, height = 6)
    private String adId;
    /**
     * 筛客名称
     */
    @Excel(name = "筛客名称", width = 20, height = 6)
    private String promoterName;
    /**
     * 老客姓名
     */
    @Excel(name = "老客姓名", width = 20, height = 6)
    private String oldKzName;
    /**
     * 老客电话
     */
    @Excel(name = "老客电话", width = 20, height = 6)
    private String oldKzPhone;


    public String getInvalidMemo() {
        return invalidMemo;
    }

    public void setInvalidMemo(String invalidMemo) {
        this.invalidMemo = invalidMemo;
    }

    public String getOldKzName() {
        return oldKzName;
    }

    public void setOldKzName(String oldKzName) {
        this.oldKzName = oldKzName;
    }

    public String getOldKzPhone() {
        return oldKzPhone;
    }

    public void setOldKzPhone(String oldKzPhone) {
        this.oldKzPhone = oldKzPhone;
    }

    public String getShootType() {
        return shootType;
    }

    public void setShootType(String shootType) {
        this.shootType = shootType;
    }

    public String getReceptorName() {
        return receptorName;
    }

    public void setReceptorName(String receptorName) {
        this.receptorName = receptorName;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public void setPromoterName(String promoterName) {
        this.promoterName = promoterName;
    }

    public String getComeShopTime() {
        return comeShopTime;
    }

    public void setComeShopTime(String comeShopTime) {
        this.comeShopTime = comeShopTime;
    }

    public String getKzSex() {
        return kzSex;
    }

    public void setKzSex(String kzSex) {
        this.kzSex = kzSex;
    }

    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceivePeriod() {
        return receivePeriod;
    }

    public void setReceivePeriod(String receivePeriod) {
        this.receivePeriod = receivePeriod;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStayAmount() {
        return stayAmount;
    }

    public void setStayAmount(int stayAmount) {
        this.stayAmount = stayAmount;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getSuccessPeriod() {
        return successPeriod;
    }

    public void setSuccessPeriod(String successPeriod) {
        this.successPeriod = successPeriod;
    }

    public String getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(String yxLevel) {
        this.yxLevel = yxLevel;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(String zxStyle) {
        this.zxStyle = zxStyle;
    }
}
