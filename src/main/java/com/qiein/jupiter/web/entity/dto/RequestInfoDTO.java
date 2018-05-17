package com.qiein.jupiter.web.entity.dto;

/**
 * 当前登录详细信息
 *
 * @Author: shiTao
 */
public class RequestInfoDTO {
    /**
     * url
     */
    private String url;
    /**
     * 参数
     */
    private String params;
    /**
     * Ip
     */
    private String ip;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
