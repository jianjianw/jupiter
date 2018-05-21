package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 钉钉
 *
 * @Author: shiTao
 */
public class DingAuthDTO implements Serializable {

    private static final long serialVersionUID = 7643712184216258888L;

    private String accessToken;

    private String persistentCode;

    private String authCode;

    private String openId;

    private String unionId;

    private String snsToken;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSnsToken() {
        return snsToken;
    }

    public void setSnsToken(String snsToken) {
        this.snsToken = snsToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPersistentCode() {
        return persistentCode;
    }

    public void setPersistentCode(String persistentCode) {
        this.persistentCode = persistentCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
