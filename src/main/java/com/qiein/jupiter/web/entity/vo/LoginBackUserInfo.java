package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 登录成功用户
 *
 * @Author: shiTao
 */
public class LoginBackUserInfo implements Serializable {

    private static final long serialVersionUID = -47700400013765175L;
    /**
     * 用户Id
     */
    private int uid;
    /**
     * 公司Id
     */
    private int cid;
    /**
     * token
     */
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
