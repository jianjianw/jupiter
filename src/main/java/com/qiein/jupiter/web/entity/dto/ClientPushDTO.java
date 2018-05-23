package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

/**
 * 客资推送时封装客资信息
 *
 * @author JingChenglong 2018/05/08 10:39
 */
public class ClientPushDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推送时间间隔
     */
    private int pushInterval;


    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 主键ID
     */
    private int id;

    /**
     * 客资ID
     */
    private String kzId;

    /**
     * 状态ID
     */
    private int statusId;

    /**
     * 客服ID
     */
    private int appointorId;
    /**
     * 来源ID
     */
    private int sourceId;
    /**
     * 邀约名称
     */
    private String appointName;

    /**
     * 最终拍摄地名称
     */
    private String filmingArea;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPushInterval() {
        return pushInterval;
    }

    public void setPushInterval(int pushInterval) {
        this.pushInterval = pushInterval;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAppointorId() {
        return appointorId;
    }

    public void setAppointorId(int appointorId) {
        this.appointorId = appointorId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getFilmingArea() {
        return filmingArea;
    }

    public void setFilmingArea(String filmingArea) {
        this.filmingArea = filmingArea;
    }
}