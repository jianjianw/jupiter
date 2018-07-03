package com.qiein.jupiter.web.entity.po;

public class WechatScanPO {
    private Integer  id;
    private String kzId;
    private String kzName;
    private String kzPhone;
    private String kzqq;
    private String kzWechat;
    private String createTime;
    private String appointName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
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

    public String getKzqq() {
        return kzqq;
    }

    public void setKzqq(String kzqq) {
        this.kzqq = kzqq;
    }

    public String getKzWechat() {
        return kzWechat;
    }

    public void setKzWechat(String kzWechat) {
        this.kzWechat = kzWechat;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }
}
