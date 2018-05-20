package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 微信验证dto
 *
 * @Author: shiTao
 */
public class WeChatAuthDTO implements Serializable {
    private static final long serialVersionUID = -2801754196237630159L;

    private String accessToken;

    private String authCode;

    private String openId;

    private String unionId;

    private String headImg;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
