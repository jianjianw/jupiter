package com.qiein.jupiter.web.entity.po;

public class RepateKzLogPO {


    private Integer id;
    private String kzId;
    private String kzName;
    private String kzPhone;
    private String kzqq;
    private String kzWechat;
    private String appointName;
    private String memo;
    private String collectorName;
    private Integer count;
    private Integer statusId;
    private Integer sourseId;

    public Integer getSourseId() {
        return sourseId;
    }

    public void setSourseId(Integer sourseId) {
        this.sourseId = sourseId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


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

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
