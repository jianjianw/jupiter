package com.qiein.jupiter.web.entity.vo;
/**
 * 展示发送短信记录
 *
 * @Author:xianglaing
 * @date:2018/6/8
 */
public class SendMsgShowVO {
    //公司名称
    private String companyName;
    //发送手机号
    private String phone;
    //发送时间
    private String sendTime;
    //发送模板名称
    private String title;
    //发送价格
    private String price;
    //短信内容
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
