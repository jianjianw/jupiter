package com.qiein.jupiter.web.entity.dto;

public class SendMsgToDTO {
    private String signname;
    private SendMsgDTO params;

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    public SendMsgDTO getParams() {
        return params;
    }

    public void setParams(SendMsgDTO params) {
        this.params = params;
    }
}
