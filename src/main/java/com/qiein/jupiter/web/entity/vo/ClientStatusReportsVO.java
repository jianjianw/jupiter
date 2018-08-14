package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * @author: yyx
 * @Date: 2018-8-14
 */
public class ClientStatusReportsVO implements Serializable {
    /**
     * 状态id
     * */
    private Integer statusId;

    /**
     * 状态名称
     * */
    private String statusName;

    /**
     * 客资数量
     * */
    private int kzNum;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getKzNum() {
        return kzNum;
    }

    public void setKzNum(int kzNum) {
        this.kzNum = kzNum;
    }


}
