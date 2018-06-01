package com.qiein.jupiter.web.entity.vo;

/**
 * Created by Tt
 * on 2018/6/1 0001.
 */
public class StaffMsg {
    private Integer id;
    private Integer companyId;
    private String msgSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getMsgSet() {
        return msgSet;
    }

    public void setMsgSet(String msgSet) {
        this.msgSet = msgSet;
    }
}
