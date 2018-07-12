package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;

/**
 * 登录用户业务对象
 */
public class LoginUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotEmptyStr(message = "{loginUser.phone.null}")
    private String userName;

    /**
     * 密码
     */
    @NotEmptyStr(message = "{loginUser.password.null}")
    private String password;

    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 是否是客户端
     */
    private boolean clientFlag;
    /**
     * 客户端版本
     */
    private int clientVersion;
    /**
     * ip
     */
    private String ip;
    /**
     * mac
     */
    private String mac;

    /**
     * 是否移动端
     */
    private boolean isMobile;

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setClientFlag(boolean clientFlag) {
        this.clientFlag = clientFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isClientFlag() {
        return clientFlag;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


}
