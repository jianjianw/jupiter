package com.qiein.jupiter.web.entity.vo;

/**
 * 网销排班 分配日志
 * author xiangliang
 */
public class AllotLogVO{
    private Integer id;
    private String allotTime;
    private String kzName;
    private String kzPhone;
    private String kzWechat;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(String allotTime) {
        this.allotTime = allotTime;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }

    public String getKzPhone() {
        return kzPhone;
    }

    public void setKzPhone(String kzPhone) {
        this.kzPhone = kzPhone;
    }

    public String getKzWechat() {
        return kzWechat;
    }

    public void setKzWechat(String kzWechat) {
        this.kzWechat = kzWechat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
