package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;

import java.io.Serializable;

/**
 * 客资实体类
 */
public class ClientVO implements Serializable {
    private static final long serialVersionUID = 2657896097650380311L;
    /**
     * 性别（0-未知，1-男，2-女）
     */
    private int sex;
    /**
     * 客资姓名
     */
    private String kzName;
    /**
     * 客资手机号
     */
    private String kzPhone;
    /**
     * 客资微信
     */
    private String kzWechat;
    /**
     * 客资旺旺
     */
    private String kzWw;
    /**
     * 客资QQ
     */
    private String kzQq;
    /**
     * 客资咨询方式
     */
    private String zxStyle;
    /**
     * 拍摄地
     */
    @Id(message = "{client.shopId.null}")
    private int shopId;
    /**
     * 渠道ID
     */
    @Id(message = "{client.channelId.null}")
    private int channelId;
    /**
     * 来源ID
     */
    @Id(message = "{client.sourceId.null}")
    private int sourceId;
    /**
     * 拍摄类型ID
     */
    private int typeId;
    /**
     * 广告着陆页
     */
    private String adAddress;
    /**
     * 广告ID
     */
    private String adId;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 地址
     */
    private String address;
    /**
     * 推广备注
     */
    private String remark;
    /**
     * 邀约客服ID
     */
    private int appointId;
    /**
     * 邀约客服小组ID
     */
    private String groupId;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 推广人ID
     */
    private int collectorId;
    /**
     * 推广人姓名
     */
    private String collectorName;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 拍摄地名称
     */
    private String shopName;
    /**
     * 邀约客服姓名
     */
    private String appointName;
    /**
     * 邀约客服小组名
     */
    private String groupName;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
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

    public String getKzWw() {
        return kzWw;
    }

    public void setKzWw(String kzWw) {
        this.kzWw = kzWw;
    }

    public String getKzQq() {
        return kzQq;
    }

    public void setKzQq(String kzQq) {
        this.kzQq = kzQq;
    }

    public String getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(String zxStyle) {
        this.zxStyle = zxStyle;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getAdAddress() {
        return adAddress;
    }

    public void setAdAddress(String adAddress) {
        this.adAddress = adAddress;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAppointId() {
        return appointId;
    }

    public void setAppointId(int appointId) {
        this.appointId = appointId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
