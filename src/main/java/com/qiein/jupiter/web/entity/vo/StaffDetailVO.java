package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 员工详细信息
 */
public class StaffDetailVO extends StaffPO {

    private static final long serialVersionUID = 7687459749239616046L;
    /**
     * 微信名称
     */
    private String weChatName;
    /**
     * 微信头像
     */
    private String weChatImg;
    /**
     * qq
     */
    private String qq;
    /**
     * 上次登录时间
     */
    private int lastLoginTime;
    /**
     * 上传登录Ip
     */
    private String lastLoginIp;
    /**
     * openid
     */
    private String openId;
    /**
     * 创建时间
     */
    private int createTime;

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

    @Override
    public String getOpenId() {
        return openId;
    }

    @Override
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
