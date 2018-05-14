package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.druid.util.StringUtils;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;

/**
 * 导入客资实体类
 */
public class ClientExcelDTO implements Serializable {

    private static final long serialVersionUID = -7350905349670754997L;
    /**
     * 客资编码
     */
    private String kzId;
    /**
     * 类型ID
     */
    private int typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 分类ID
     */
    private int classId;
    /**
     * 分类名称
     */
    private String className;

    /**
     * 客资电话
     */
    @Excel(name = "手机号")
    private String kzPhone;
    /**
     * 客资微信
     */
    @Excel(name = "微信号")
    private String kzWechat;
    /**
     * 客资QQ
     */
    @Excel(name = "QQ")
    private String kzQq;
    /**
     * 客资旺旺
     */
    @Excel(name = "旺旺")
    private String kzWw;
    /**
     * 性别（0男 1女 9未知）
     */
    private String sex;
    /**
     * 状态ID
     */
    private int statusId;
    /**
     * 状态名称
     */
    private String statusName;
    /**
     * 渠道ID
     */
    private int channelId;
    /**
     * 渠道名
     */
    @Excel(name = "渠道")
    private String channelName;
    /**
     * 渠道ID
     */
    private int sourceId;
    /**
     * 渠道名称
     */
    @Excel(name = "来源")
    private String sourceName;

    /**
     * 推广员ID
     */
    private int collectorId;
    /**
     * 推广员姓名
     */
    @Excel(name = "鼠标手")
    private String collectorName;

    /**
     * 邀约客服ID
     */
    private int appointId;
    /**
     * 邀约客服姓名
     */
    @Excel(name = "客服")
    private String appointName;
    /**
     * 推广备注
     */
    @Excel(name = "鼠标手备注")
    private String remark;
    /**
     * 录入时间接收格式
     */
    @Excel(name = "录入时间", databaseFormat = "MM月dd日", format = "yyyy-MM-dd HH:mm:ss")
    private double time;
    /**
     * 录入时间db
     */
    private long createTime;
    /**
     * 拍摄地ID
     */
    private int shopId;
    /**
     * 拍摄地名称
     */
    @Excel(name = "拍摄地")
    private String shopName;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 操作人ID
     */
    private int operaId;
    /**
     * 关键词
     */
    @Excel(name = "关键字")
    private String keyWord;
    /**
     * 客服组ID
     */
    private String groupId;
    /**
     * 客服组名称
     */
    @Excel(name = "组别")
    private String groupName;
    /**
     * 是否有效
     */
    @Excel(name = "有/无效")
    private String validFlag;
    /**
     * 省份地址
     */
    @Excel(name = "省份")
    private String address;

    public boolean isWrongInfo() {
        if (NumUtil.isNull(getCollectorId()) || NumUtil.isNull(getSourceId())
                || (StringUtil.isEmpty(getKzPhone()) && StringUtil.isEmpty(getKzWechat())
                && StringUtil.isEmpty(getKzQq()) && StringUtil.isEmpty(getKzWw())) || NumUtil.isNull(getShopId())
                ) {
            return true;
        }
        return false;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public int getAppointId() {
        return appointId;
    }

    public void setAppointId(int appointId) {
        this.appointId = appointId;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getOperaId() {
        return operaId;
    }

    public void setOperaId(int operaId) {
        this.operaId = operaId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
