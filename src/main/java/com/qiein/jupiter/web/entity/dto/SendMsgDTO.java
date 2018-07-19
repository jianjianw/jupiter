package com.qiein.jupiter.web.entity.dto;

import java.util.Map;

/**
 * 发送短信所需参数
 *
 * @Author:xianglaing
 * @date:2018/5/24
 */
public class SendMsgDTO {
    //公司id
    private int companyId;
    //短信模板id
    private String templateId;
    //短信模板类型
    private String templateType;
    //手机号码
    private String phone;
    //需要短信发送的变量 是具体而定
    private Map<String,String> map;
    //短信内容
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
