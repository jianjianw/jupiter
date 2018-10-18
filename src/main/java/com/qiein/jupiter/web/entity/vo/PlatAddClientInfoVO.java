package com.qiein.jupiter.web.entity.vo;

/**
 * 平台新增客资的对象
 *
 * @Author: shiTao
 */
public class PlatAddClientInfoVO {
    /**
     * 客资ID
     */

    private String kzId;

    /**
     * 性別
     */
    private int sex;

    /**
     * 姓名
     */
    private String kzName;

    /**
     * 电话
     */
    private String kzPhone;

    /**
     * 微信
     */
    private String kzWechat;

    /**
     * QQ
     */
    private String kzQq;

    /**
     * 旺旺
     */
    private String kzWw;

    /**
     * 渠道ID
     */
    private int channelId;

    /**
     * 渠道名
     */
    private String channelName;

    /**
     * 渠道类型
     */
    private int srcType;

    /**
     * 来源ID
     */
    private int sourceId;

    /**
     * 来源名
     */
    private String sourceName;

    /**
     * 门店/拍摄地ID
     */
    private int shopId;

    /**
     * 门店名/拍摄地名
     */
    private String shopName;

    /**
     * 咨询方式
     */
    private int zxStyle;

    /**
     * 关键词
     */
    private String keyWord;

    /**
     * 广告着陆页
     */
    private String adAddress;

    /**
     * 广告ID
     */
    private String adId;

    /**
     * 咨询类型ID
     */
    private int typeId;

    /**
     * 客人地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提报备注
     */
    private String memo;

    /**
     * 客服ID
     */
    private int appointId;

    /**
     * 客服名称
     */
    private String appointName;

    /**
     * 客服组ID
     */
    private String groupId;

    /**
     * 客服组名称
     */
    private String groupName;

    /**
     * 状态ID
     */
    private int statusId;

    /**
     * 分类ID
     */
    private int classId;

    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 录入人ID
     */
    private int collectorId;

    /**
     * 录入人姓名
     */
    private String collectorName;

    /**
     * 筛客人ID
     */
    private int promotorId;

    /**
     * 筛客人名称
     */
    private String promotorName;

    /**
     * 分配规则类型
     */
    private int allotTyle;

    /**
     * 附件图片地址
     */
    private String talkImg;
    /**
     * 是否筛选
     */
    private String filtFlag;
    /**
     * 配偶姓名
     */
    private String mateName;
    /**
     * 配偶手机号
     */
    private String matePhone;
    /**
     * 配偶微信
     */
    private String mateWechat;
    /**
     * 配偶QQ
     */
    private String mateQq;
    /**
     * 预算范围
     */
    private int ysRange;
    /**
     * 婚期时间
     */
    private int marryTime;
    /**
     * 预拍时间
     */
    private int ypTime;
    /**
     * 老客姓名
     */
    private String oldKzName;
    /**
     * 老客手机号
     */
    private String oldKzPhone;
    /**
     * 成交套系金额
     */
    private int amount;
    /**
     * 到店时间
     */
    private int comeShopTime;
    /**
     * 门市接待员ID
     */
    private int receptorId;
    /**
     * 门市接待员姓名
     */
    private String receptorName;
    /**
     * 无效原因
     */
    private String invalidLabel;
    /**
     * 已收金额
     */
    private int stayAmount;
    /**
     * 支付方式
     */
    private int payStyle;
    /**
     * 合同编号
     */
    private String htNum;
    /**
     * 订单时间
     */
    private int successTime;
    /**
     * 预约时间
     */
    private int appointTime;
    /**
     * 订单图片
     */
    private String orderImg;
    /**
     * 意向等级
     */
    private int yxLevel;
    /**
     * 操作人ID
     */
    private int operaId;
    /**
     * 操作人姓名
     */
    private String operaName;

    /**
     * 收款时间
     */
    private int stayTime;
    /**
     * 收款时间
     */
    private int payTime;
    /**
     * 收款人ID
     */
    private int receiptId;
    /**
     * 收款人姓名
     */
    private String receiptName;
    /**
     * 本次收款金额
     */
    private int payamount;

    public int getSrcType() {
        return srcType;
    }

    public void setSrcType(int srcType) {
        this.srcType = srcType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTalkImg() {
        return talkImg;
    }

    public void setTalkImg(String talkImg) {
        this.talkImg = talkImg;
    }

    public int getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(int promotorId) {
        this.promotorId = promotorId;
    }

    public String getPromotorName() {
        return promotorName;
    }

    public void setPromotorName(String promotorName) {
        this.promotorName = promotorName;
    }

    public int getAllotTyle() {
        return allotTyle;
    }

    public void setAllotTyle(int allotTyle) {
        this.allotTyle = allotTyle;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

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

    public int getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(int zxStyle) {
        this.zxStyle = zxStyle;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
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

    public String getFiltFlag() {
        return filtFlag;
    }

    public void setFiltFlag(String filtFlag) {
        this.filtFlag = filtFlag;
    }

    public String getMateName() {
        return mateName;
    }

    public void setMateName(String mateName) {
        this.mateName = mateName;
    }

    public String getMatePhone() {
        return matePhone;
    }

    public void setMatePhone(String matePhone) {
        this.matePhone = matePhone;
    }

    public String getMateWechat() {
        return mateWechat;
    }

    public void setMateWechat(String mateWechat) {
        this.mateWechat = mateWechat;
    }

    public String getMateQq() {
        return mateQq;
    }

    public void setMateQq(String mateQq) {
        this.mateQq = mateQq;
    }

    public int getYsRange() {
        return ysRange;
    }

    public void setYsRange(int ysRange) {
        this.ysRange = ysRange;
    }

    public int getMarryTime() {
        return marryTime;
    }

    public void setMarryTime(int marryTime) {
        this.marryTime = marryTime;
    }

    public int getYpTime() {
        return ypTime;
    }

    public void setYpTime(int ypTime) {
        this.ypTime = ypTime;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getComeShopTime() {
        return comeShopTime;
    }

    public void setComeShopTime(int comeShopTime) {
        this.comeShopTime = comeShopTime;
    }

    public int getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(int receptorId) {
        this.receptorId = receptorId;
    }

    public String getReceptorName() {
        return receptorName;
    }

    public void setReceptorName(String receptorName) {
        this.receptorName = receptorName;
    }

    public String getInvalidLabel() {
        return invalidLabel;
    }

    public void setInvalidLabel(String invalidLabel) {
        this.invalidLabel = invalidLabel;
    }

    public int getStayAmount() {
        return stayAmount;
    }

    public void setStayAmount(int stayAmount) {
        this.stayAmount = stayAmount;
    }

    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    public String getHtNum() {
        return htNum;
    }

    public void setHtNum(String htNum) {
        this.htNum = htNum;
    }

    public int getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(int successTime) {
        this.successTime = successTime;
    }

    public int getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(int appointTime) {
        this.appointTime = appointTime;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public int getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(int yxLevel) {
        this.yxLevel = yxLevel;
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

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getPayTime() {
        return payTime;
    }

    public void setPayTime(int payTime) {
        this.payTime = payTime;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public int getPayamount() {
        return payamount;
    }

    public void setPayamount(int payamount) {
        this.payamount = payamount;
    }

}
