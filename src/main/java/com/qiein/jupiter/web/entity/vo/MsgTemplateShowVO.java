package com.qiein.jupiter.web.entity.vo;


import java.util.List;

public class MsgTemplateShowVO {
    private String balance;
    private List<MsgTemplateVO> msgTemplateVOS;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<MsgTemplateVO> getMsgTemplateVOS() {
        return msgTemplateVOS;
    }

    public void setMsgTemplateVOS(List<MsgTemplateVO> msgTemplateVOS) {
        this.msgTemplateVOS = msgTemplateVOS;
    }
}
