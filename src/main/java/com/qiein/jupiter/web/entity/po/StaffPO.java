package com.qiein.jupiter.web.entity.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.aop.validate.annotation.Phone;
import com.qiein.jupiter.web.entity.BaseEntity;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;

/**
 * 员工数据库对象
 */
public class StaffPO extends BaseEntity {

    private static final long serialVersionUID = 3690836371907136145L;

    /**
     * 昵称
     */
    @NotEmptyStr(message = "{staff.name.null}")
    @Excel(name="艺名")
    private String nickName;
    /**
     * 手机号码
     */
    @NotEmptyStr(message = "{staff.phone.null}")
    @Phone
    @Excel(name="手机")
    private String phone;
    /**
     * 全名
     */
    @NotEmptyStr(message = "{staff.userName.null}")
    @Excel(name="全名")
    private String userName;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 钉钉企业ID
     */
    private String corpId;
    /**
     * 钉钉用户ID
     */
    private String dingUserId;
    /**
     * 头像
     */
    private String headImg;
    /**
     * token
     */
    private String token;
    /**
     * 在线状态 0 下线 1 在线
     */
    private int statusFlag;
    /**
     * 是否锁定
     */
    private boolean lockFlag;
    /**
     * 是否删除
     */
    private boolean delFlag;

    private int todayNum;

    private int weight;

    private int limitDay;

    private int lastPushTime;

    private String limitChannelIds;

    private String limitShopIds;

    public int getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(int todayNum) {
        this.todayNum = todayNum;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public int getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(int lastPushTime) {
        this.lastPushTime = lastPushTime;
    }

    public String getLimitChannelIds() {
        return limitChannelIds;
    }

    public void setLimitChannelIds(String limitChannelIds) {
        this.limitChannelIds = limitChannelIds;
    }

    public String getLimitShopIds() {
        return limitShopIds;
    }

    public void setLimitShopIds(String limitShopIds) {
        this.limitShopIds = limitShopIds;
    }

    public StaffPO() {

    }

    /**
     * 根据员工详细信息初始化一个类
     *
     * @param staffDetailVO
     */
    public StaffPO(StaffDetailVO staffDetailVO) {
        this.setId(staffDetailVO.getId());
        this.nickName = staffDetailVO.getNickName();
        this.phone = staffDetailVO.getPhone();
        this.userName = staffDetailVO.getUserName();
        this.password = staffDetailVO.getPassword();
        this.companyId = staffDetailVO.getCompanyId();
        this.corpId = staffDetailVO.getCorpId();
        this.dingUserId = staffDetailVO.getDingUserId();
        this.headImg = staffDetailVO.getHeadImg();
        this.token = staffDetailVO.getToken();
        this.lockFlag = staffDetailVO.isLockFlag();
        this.delFlag = staffDetailVO.isDelFlag();
        this.statusFlag=staffDetailVO.getStatusFlag();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }
}