package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 员工详细信息
 */
public class StaffDetailPO extends BaseEntity {

    private static final long serialVersionUID = 1627067701096255485L;
    /**
     * 微信名称
     */
    private String weChatName;
    /**
     * 微信头像
     */
    private String weChatImg;
    /**
     * 上次登录时间
     */
    private int lastLoginTime;
    /**
     * 上次登录IP
     */
    private String lastLoginIp;
    /**
     * Openid
     */
    private String openId;
    /**
     * 创建时间
     */
    private int createTime;
    /**
     * 公司ID
     */
    private int companyId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWeChatName() {
        return weChatName;
    }

    public void setWeChatName(String weChatName) {
        this.weChatName = weChatName;
    }

    public String getWeChatImg() {
        return weChatImg;
    }

    public void setWeChatImg(String weChatImg) {
        this.weChatImg = weChatImg;
    }

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
