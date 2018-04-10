package com.qiein.jupiter.web.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 员工数据库对象
 */
public class StaffPO extends BaseEntity {

    private static final long serialVersionUID = 3690836371907136145L;

    /**
     * 昵称
     */
    @NotEmpty(message = "{staff.name.null}")
    private String nickName;
    /**
     * 手机号码
     */
    @NotEmpty(message = "{staff.phone.null}")
    private String phone;
    /**
     * 全名
     */
    @NotEmpty(message = "{staff.userName.null}")
    private String userName;
    /**
     * 密码
     */
    @NotEmpty(message = "{staff.password.null}")
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
     * 微信
     */
    private String openId;
    /**
     * 头像
     */
    private String headImg;
    /**
     * token
     */
    private String token;
    /**
     * 是否在线
     */
    private int showFlag;
    /**
     * 是否锁定
     */
    private boolean lockFlag;

    /**
     * 是否删除
     */
    private boolean delFlag;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public int getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(int showFlag) {
        this.showFlag = showFlag;
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
}