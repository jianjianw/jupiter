package com.qiein.jupiter.web.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * FileName: ClientExcelNewsDTO
 *
 * @author: yyx
 * @Date: 2018-6-15 14:52
 */
public class ClientExcelNewsDTO implements Serializable {
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
    @Excel(name = "咨询类型（必填-匹配后台）")
    private String typeName;
    /**
     * 分类ID
     */
    private int classId;
    /**
     * 分类名称
     */
    @Excel(name = "状态（必填-匹配后台）")
    private String className;
    /**
     * 咨询方式
     * */
    @Excel(name="咨询方式")
    private String zxStyleStr;
    /**
     * 咨询方式
     * */
    private int zxStyle;

    /**
     * 姓名
     * */
    @Excel(name="姓名")
    private String kzName;
    /**
     * 客资电话
     */
    @Excel(name = "电话")
    private String kzPhone;
    /**
     * 客资微信
     */
    @Excel(name = "微信")
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
    @Excel(name = "性别", replace = {"男_0", "女_1", "未知_9"})
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
    private String channelName;
    /**
     * 渠道ID
     */
    private int sourceId;
    /**
     * 渠道名称
     */
    @Excel(name = "来源（必填-匹配后台）")
    private String sourceName;

    /**
     * 推广员ID
     */
    private int collectorId;
    /**
     * 推广员姓名
     */
    @Excel(name = "推广（提报人）")
    private String collectorName;

    /**
     * 邀约客服ID
     */
    private int appointorId;
    /**
     * 邀约客服姓名
     */
    @Excel(name = "客服（邀约员）")
    private String appointName;
    /**
     * 推广电话
     */
    @Excel(name = "推广电话")
    private String appointNamePhone;

    /**
     * 老客姓名
     */
    @Excel(name = "老客姓名")
    private String oldKzName;
    /**
     * 老客电话
     */
    @Excel(name = "老客电话")
    private String oldKzPhone;

    /**
     * 推广备注
     */
    @Excel(name = "备注")
    private String remark;
    /**
     * 录入时间接收格式
     */
    @Excel(name = "提报时间", databaseFormat = "MM月dd日", format = "yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss,yyyy-MM-dd HH:mm")
    private Date time;
    /**
     * 预约时间接受格式
     */
    @Excel(name = "预约时间", databaseFormat = "MM月dd日", format = "yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss,yyyy-MM-dd HH:mm")
    private Date appointTimeDate;
    /**
     * 预约时间
     * */
    private long appointTime;
    /**
     * 入店时间接受格式
     */
    @Excel(name = "入店时间 (首次入店时间，邀约计算业绩)", databaseFormat = "MM月dd日", format = "yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss,yyyy-MM-dd HH:mm")
    private Date comeShopTimeDate;

    /**
     * 入店时间
     * */
    private long comeShopTime;
    /**
     * 成交时间接受格式
     */
    @Excel(name = "成交时间", databaseFormat = "MM月dd日", format = "yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss,yyyy-MM-dd HH:mm")
    private Date successTimeDate;
    /**
     * 成交时间
     * */
    private long successTime;
    /**
     * 婚期时间接受格式
     */
    @Excel(name = "婚期时间")
    private String marryTimeStr;
    /**
     * 婚期时间
     * */
    private int marryTime;
    /**
     * 预拍时间
     */
    @Excel(name = "预拍时间")
    private String ypTimeStr;
    /**
     * 预拍时间
     * */
    private int ypTime;

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
    @Excel(name = "门店")
    private String shopName;
    /**
     * 成交套系
     */
    private Integer amount;
    /**
     * 成交套系金额
     */
    @Excel(name = "成交套系")
    private String amountStr;
    /**
     * 门市id
     */
    private Integer receptorId;
    /**
     * 门市姓名
     */
    @Excel(name = "门市")
    private String receptorName;
    /**
     * 配偶姓名
     */
    @Excel(name = "配偶姓名")
    private String mateName;
    /**
     * 配偶电话
     */
    @Excel(name = "配偶电话")
    private String matePhone;
    /**
     * 配偶微信
     */
    @Excel(name = "配偶微信")
    private String mateWeChat;
    /**
     * 配偶QQ
     */
    @Excel(name = "配偶QQ")
    private String mateQq;
    /**
     * 广告id
     */
    @Excel(name = "广告id")
    private String adId;
    /**
     * 广告着陆页
     */
    @Excel(name = "广告着陆页")
    private String adAddress;

    /**
     * 意向等级Str
     */
    @Excel(name = "意向等级")
    private String yxLevelStr;
    /**
     * 意向等级
     */
    private int yxLevel;
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
    @Excel(name = "地区")
    private String address;
    /**
     * 拍摄编码
     */
    private String filmingCode;
    /**
     * 拍摄地
     */
    @Excel(name = "拍摄地点")
    private String filmingArea;

    /**
     * 预算范围Str
     */
    @Excel(name = "预算范围")
    private String ysRangeStr;
    /**
     * 预算范围
     */
    private int ysRange;

    /**
     * 无效原因
     */
    @Excel(name = "无效原因/流失原因")
    private String invalidLabel;

    /**
     * 已收金额Str
     */
    @Excel(name = "已收金额")
    private String stayaMountStr;
    /**
     * 已收金额
     */
    private Integer stayaMount;

    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    private String htNum;
    /**
     * 客资ID拼接
     */
    private String kzIds;
    /**
     * 是否设置成当前时间
     */
    private boolean currentTime;
    /**
     * 预约进店时间
     * */
    private String appointTimeStr;
    /**
     * 首次进店时间
     * */
    private String comeShopTimeStr;
    /**
     * 订单时间
     * */
    private String successTimeStr;


    public int getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(int zxStyle) {
        this.zxStyle = zxStyle;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getAppointTimeStr() {
        return appointTimeStr;
    }

    public void setAppointTimeStr(String appointTimeStr) {
        this.appointTimeStr = appointTimeStr;
    }

    public String getComeShopTimeStr() {
        return comeShopTimeStr;
    }

    public void setComeShopTimeStr(String comeShopTimeStr) {
        this.comeShopTimeStr = comeShopTimeStr;
    }

    public String getSuccessTimeStr() {
        return successTimeStr;
    }

    public void setSuccessTimeStr(String successTimeStr) {
        this.successTimeStr = successTimeStr;
    }

    public boolean checkWrongInfo() {
        if (NumUtil.isNull(getTypeId()) || NumUtil.isNull(getChannelId()) || NumUtil.isNull(getSourceId())
                || (StringUtil.isEmpty(getKzPhone())  && StringUtil.isEmpty(getKzWechat())
                && StringUtil.isEmpty(getKzQq()))
                || NumUtil.isNull(getShopId())) {
            return true;
        }
        if(!StringUtil.checkWeChat(getKzWechat()) || !RegexUtil.checkMobile(getKzPhone()) || !StringUtil.isQQCorrect(getKzQq())){
            return true;
        }
        return false;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getAppointTimeDate() {
        return appointTimeDate;
    }

    public void setAppointTimeDate(Date appointTimeDate) {
        this.appointTimeDate = appointTimeDate;
    }

    public Date getComeShopTimeDate() {
        return comeShopTimeDate;
    }

    public void setComeShopTimeDate(Date comeShopTimeDate) {
        this.comeShopTimeDate = comeShopTimeDate;
    }

    public Date getSuccessTimeDate() {
        return successTimeDate;
    }

    public void setSuccessTimeDate(Date successTimeDate) {
        this.successTimeDate = successTimeDate;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }




    public String getMarryTimeStr() {
        return marryTimeStr;
    }

    public void setMarryTimeStr(String marryTimeStr) {
        this.marryTimeStr = marryTimeStr;
    }

    public String getYpTimeStr() {
        return ypTimeStr;
    }

    public void setYpTimeStr(String ypTimeStr) {
        this.ypTimeStr = ypTimeStr;
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

    public int getAppointorId() {
        return appointorId;
    }

    public void setAppointorId(int appointorId) {
        this.appointorId = appointorId;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getAppointNamePhone() {
        return appointNamePhone;
    }

    public void setAppointNamePhone(String appointNamePhone) {
        this.appointNamePhone = appointNamePhone;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public long getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(long appointTime) {
        this.appointTime = appointTime;
    }

    public long getComeShopTime() {
        return comeShopTime;
    }

    public void setComeShopTime(long comeShopTime) {
        this.comeShopTime = comeShopTime;
    }

    public long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(long successTime) {
        this.successTime = successTime;
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

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdAddress() {
        return adAddress;
    }

    public void setAdAddress(String adAddress) {
        this.adAddress = adAddress;
    }

    public String getYxLevelStr() {
        return yxLevelStr;
    }

    public void setYxLevelStr(String yxLevelStr) {
        this.yxLevelStr = yxLevelStr;
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

    public String getFilmingCode() {
        return filmingCode;
    }

    public void setFilmingCode(String filmingCode) {
        this.filmingCode = filmingCode;
    }

    public String getFilmingArea() {
        return filmingArea;
    }

    public void setFilmingArea(String filmingArea) {
        this.filmingArea = filmingArea;
    }

    public String getYsRangeStr() {
        return ysRangeStr;
    }

    public void setYsRangeStr(String ysRangeStr) {
        this.ysRangeStr = ysRangeStr;
    }


    public String getInvalidLabel() {
        return invalidLabel;
    }

    public void setInvalidLabel(String invalidLabel) {
        this.invalidLabel = invalidLabel;
    }

    public String getStayaMountStr() {
        return stayaMountStr;
    }

    public void setStayaMountStr(String stayaMountStr) {
        this.stayaMountStr = stayaMountStr;
    }

    public Integer getStayaMount() {
        return stayaMount;
    }

    public void setStayaMount(Integer stayaMount) {
        this.stayaMount = stayaMount;
    }

    public String getHtNum() {
        return htNum;
    }

    public void setHtNum(String htNum) {
        this.htNum = htNum;
    }

    public String getKzIds() {
        return kzIds;
    }

    public void setKzIds(String kzIds) {
        this.kzIds = kzIds;
    }

    public boolean isCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(boolean currentTime) {
        this.currentTime = currentTime;
    }


    public String getZxStyleStr() {
        return zxStyleStr;
    }

    public void setZxStyleStr(String zxStyleStr) {
        this.zxStyleStr = zxStyleStr;
    }


}
