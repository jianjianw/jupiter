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
    private Integer id;
    /**
     * 客资ID
     */
    private String kzId;
    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer sex;
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
    private Integer zxStyle;
    /**
     * 拍摄地
     */
    private Integer shopId;
    /**
     * 渠道ID
     */
    @Id(message = "{client.channelId.null}")
    private Integer channelId;
    /**
     * 来源ID
     */
    @Id(message = "{client.sourceId.null}")
    private Integer sourceId;
    /**
     * 拍摄类型ID
     */
    private Integer typeId;
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
    private Integer appointId;
    /**
     * 邀约客服小组ID
     */
    private String groupId;
    /**
     * 企业ID
     */
    private Integer companyId;
    /**
     * 推广人ID
     */
    private Integer collectorId;
    /**
     * 推广人姓名
     */
    private String collectorName;
    /**
     * 筛客人ID
     */
    private Integer promotorId;

    /**
     * 筛客人名称
     */
    private String promoterName;
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
    private Integer yxLevel;

    /**
     * 预算范围
     */
    private Integer ysRange;
    /**
     * 婚期时间
     */
    private Integer marryTime;
    /**
     * 预拍时间
     */
    private Integer ypTime;
    /**
     * 邀约结果
     */
    private Integer yyRst;
    /**
     * 无效原因编码
     */
    private Integer invalidCode;
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
    private Integer trackTime;
    /**
     * 消息提醒
     */
    private Integer warnStyle;
    /**
     * 成交套系金额
     */
    private Integer amount;
    /**
     * 已收金额
     */
    private Integer stayAmount;
    /**
     * 本次收款金额
     */
    private Integer payAmount;
    /**
     * 支付方式
     */
    private Integer payStyle;
    /**
     * 合同编号
     */
    private String htNum;
    /**
     * 订单时间
     */
    private Integer successTime;

    /**
     * 最终拍摄地ID
     */
    private Integer filmingCode;
    /**
     * 状态ID
     */
    private Integer statusId;
    /**
     * 领单时间
     */
    private Integer receiveTime;
    /**
     * 套系名称编码
     */
    private Integer packageCode;
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
    private Integer appointTime;

    /**
     * 门市接待人ID
     */
    private Integer receptorId;
    /**
     * 门市接待人姓名
     */
    private String receptorName;
    /**
     * 到店时间
     */
    private Integer comeShopTime;
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
    private Integer payTime;
    /**
     * 收款人ID
     */
    private Integer receiptId;
    /**
     * 收款人姓名
     */
    private String receiptName;
    /**
     * 备注修改框
     */
    private String remarkForm;

    /**
     * 操作人ID
     */
    private Integer operaId;
    /**
     * 操作人姓名
     */
    private String operaName;

    /**
     * 分配规则
     */
    private Integer allotType;
    /**
     * 渠道类型
     */
    private Integer srcType;
    /**
     * 客资大类
     */
    private Integer classId;
    /**
     * 是否筛选
     */
    private boolean filterFlag;
    /**
     * 附件图片地址
     */
    private String talkImg;

    /**
     * 什么录入的
     */
    private Integer addType;

    /**
     * 订单图片
     */
    private String orderImg;
    /**
     * 客资日志
     */
    private String log;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
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

    public Integer getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(Integer zxStyle) {
        this.zxStyle = zxStyle;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
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

    public Integer getAppointId() {
        return appointId;
    }

    public void setAppointId(Integer appointId) {
        this.appointId = appointId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public Integer getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(Integer promotorId) {
        this.promotorId = promotorId;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public void setPromoterName(String promoterName) {
        this.promoterName = promoterName;
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

    public Integer getYxLevel() {
        return yxLevel;
    }

    public void setYxLevel(Integer yxLevel) {
        this.yxLevel = yxLevel;
    }

    public Integer getYsRange() {
        return ysRange;
    }

    public void setYsRange(Integer ysRange) {
        this.ysRange = ysRange;
    }

    public Integer getMarryTime() {
        return marryTime;
    }

    public void setMarryTime(Integer marryTime) {
        this.marryTime = marryTime;
    }

    public Integer getYpTime() {
        return ypTime;
    }

    public void setYpTime(Integer ypTime) {
        this.ypTime = ypTime;
    }

    public Integer getYyRst() {
        return yyRst;
    }

    public void setYyRst(Integer yyRst) {
        this.yyRst = yyRst;
    }

    public Integer getInvalidCode() {
        return invalidCode;
    }

    public void setInvalidCode(Integer invalidCode) {
        this.invalidCode = invalidCode;
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

    public String getTrackMemo() {
        return trackMemo;
    }

    public void setTrackMemo(String trackMemo) {
        this.trackMemo = trackMemo;
    }

    public Integer getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(Integer trackTime) {
        this.trackTime = trackTime;
    }

    public Integer getWarnStyle() {
        return warnStyle;
    }

    public void setWarnStyle(Integer warnStyle) {
        this.warnStyle = warnStyle;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStayAmount() {
        return stayAmount;
    }

    public void setStayAmount(Integer stayAmount) {
        this.stayAmount = stayAmount;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Integer payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(Integer payStyle) {
        this.payStyle = payStyle;
    }

    public String getHtNum() {
        return htNum;
    }

    public void setHtNum(String htNum) {
        this.htNum = htNum;
    }

    public Integer getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Integer successTime) {
        this.successTime = successTime;
    }

    public Integer getFilmingCode() {
        return filmingCode;
    }

    public void setFilmingCode(Integer filmingCode) {
        this.filmingCode = filmingCode;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Integer receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(Integer packageCode) {
        this.packageCode = packageCode;
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

    public Integer getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Integer appointTime) {
        this.appointTime = appointTime;
    }

    public Integer getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Integer receptorId) {
        this.receptorId = receptorId;
    }

    public String getReceptorName() {
        return receptorName;
    }

    public void setReceptorName(String receptorName) {
        this.receptorName = receptorName;
    }

    public Integer getComeShopTime() {
        return comeShopTime;
    }

    public void setComeShopTime(Integer comeShopTime) {
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

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getRemarkForm() {
        return remarkForm;
    }

    public void setRemarkForm(String remarkForm) {
        this.remarkForm = remarkForm;
    }

    public Integer getOperaId() {
        return operaId;
    }

    public void setOperaId(Integer operaId) {
        this.operaId = operaId;
    }

    public String getOperaName() {
        return operaName;
    }

    public void setOperaName(String operaName) {
        this.operaName = operaName;
    }

    public Integer getAllotType() {
        return allotType;
    }

    public void setAllotType(Integer allotType) {
        this.allotType = allotType;
    }

    public Integer getSrcType() {
        return srcType;
    }

    public void setSrcType(Integer srcType) {
        this.srcType = srcType;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public boolean isFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(boolean filterFlag) {
        this.filterFlag = filterFlag;
    }

    public String getTalkImg() {
        return talkImg;
    }

    public void setTalkImg(String talkImg) {
        this.talkImg = talkImg;
    }

    public Integer getAddType() {
        return addType;
    }

    public void setAddType(Integer addType) {
        this.addType = addType;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }
}
