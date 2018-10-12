package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

import com.qiein.jupiter.aop.validate.annotation.Id;

/**
 * 客资实体类
 */
public class ClientVO implements Serializable {
    private static final long serialVersionUID = 2657896097650380311L;
    /**
     * 主键ID
     */
    private int id;
    /**
     * 客资ID
     */
    private String kzId;
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
    private int zxStyle;
    /**
     * 拍摄地
     */
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
    /**
     * 录入时间
     */
    private String createTime;
    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 销售备注
     */
    private String memo;
    /**
     * 意向等级
     */
    private int yxLevel;

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
     * 邀约结果
     */
    private int yyRst;
    /**
     * 无效原因编码
     */
    private int invalidCode;
    /**
     * 无效原因
     */
    private String invalidLabel;
    /**
     * 无效备注
     */
    private String invalidMemo;
    /**
     * 追踪备注
     */
    private String trackMemo;
    /**
     * 下次追踪时间
     */
    private int trackTime;
    /**
     * 消息提醒
     */
    private int warnStyle;
    /**
     * 成交套系金额
     */
    private int amount;
    /**
     * 已收金额
     */
    private int stayAmount;
    /**
     * 本次收款金额
     */
    private int payAmount;
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
     * 最终拍摄地ID
     */
    private int filmingCode;
    /**
     * 状态ID
     */
    private int statusId;
    /**
     * 领单时间
     */
    private int receiveTime;
    /**
     * 套系名称编码
     */
    private int packageCode;
    /**
     * 配偶姓名
     */
    private String mateName;
    /**
     * 配偶电话
     */
    private String matePhone;
    /**
     * 配偶微信
     */
    private String mateWeChat;
    /**
     * 配偶QQ
     */
    private String mateQq;
    /**
     * 预约时间
     */
    private int appointTime;

    /**
     * 门市接待人ID
     */
    private int receptorId;
    /**
     * 门市接待人姓名
     */
    private String receptorName;
    /**
     * 到店时间
     */
    private int comeShopTime;
    /**
     * 老客姓名
     */
    private String oldKzName;
    /**
     * 老客手机号
     */
    private String oldKzPhone;
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
     * 备注修改框
     */
    private String remarkForm;

    public String getRemarkForm() {
        return remarkForm;
    }

    public void setRemarkForm(String remarkForm) {
        this.remarkForm = remarkForm;
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

    public String getMateWeChat() {
        return mateWeChat;
    }

    public void setMateWeChat(String mateWeChat) {
        this.mateWeChat = mateWeChat;
    }

    public String getMateQq() {
        return mateQq;
    }

    public void setMateQq(String mateQq) {
        this.mateQq = mateQq;
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

    public int getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(int zxStyle) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(int yxLevel) {
        this.yxLevel = yxLevel;
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

    public int getYyRst() {
        return yyRst;
    }

    public void setYyRst(int yyRst) {
        this.yyRst = yyRst;
    }

    public String getInvalidLabel() {
        return invalidLabel;
    }

    public void setInvalidLabel(String invalidLabel) {
        this.invalidLabel = invalidLabel;
    }

    public String getInvalidMemo() {
        return invalidMemo;
    }

    public void setInvalidMemo(String invalidMemo) {
        this.invalidMemo = invalidMemo;
    }

    public int getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(int trackTime) {
        this.trackTime = trackTime;
    }

    public int getWarnStyle() {
        return warnStyle;
    }

    public void setWarnStyle(int warnStyle) {
        this.warnStyle = warnStyle;
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

    public int getFilmingCode() {
        return filmingCode;
    }

    public void setFilmingCode(int filmingCode) {
        this.filmingCode = filmingCode;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(int receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(int packageCode) {
        this.packageCode = packageCode;
    }

    public int getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(int appointTime) {
        this.appointTime = appointTime;
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

    public int getComeShopTime() {
        return comeShopTime;
    }

    public void setComeShopTime(int comeShopTime) {
        this.comeShopTime = comeShopTime;
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

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getTrackMemo() {
        return trackMemo;
    }

    public void setTrackMemo(String trackMemo) {
        this.trackMemo = trackMemo;
    }

    public int getInvalidCode() {
        return invalidCode;
    }

    public void setInvalidCode(int invalidCode) {
        this.invalidCode = invalidCode;
    }
}
