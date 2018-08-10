package com.qiein.jupiter.web.entity.po;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
public class CallUserPO {
    /**
     * id
     * */
    private Integer id;

    /**
     * 用户名
     * */
    private String username;

    /**
     * 密码
     * */
    private String password;

    /**
     * 用户id
     * */
    private String clientId;

    /**
     * 用户密钥
     * */
    private String clientSecret;

    /**
     * 回调地址
     * */
    private String callBakUrl;

    /**
     * 创建时间
     * */
    private Integer createTime;

    /**
     * 公司id
     * */
    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCallBakUrl() {
        return callBakUrl;
    }

    public void setCallBakUrl(String callBakUrl) {
        this.callBakUrl = callBakUrl;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
