package com.qiein.jupiter.web.entity.vo;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public class AliOauthVO {
    /**
     * accessToken
     */
    private String accessToken;

    /**
     * token_type
     * */
    private String tokenType;

    /**
     * expires_in
     * */
    private Integer expriesIn;

    /**
     * refresh_token
     * */
    private String refreshToken;

    /**
     * id_token
     * */
    private String idToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpriesIn() {
        return expriesIn;
    }

    public void setExpriesIn(Integer expriesIn) {
        this.expriesIn = expriesIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
