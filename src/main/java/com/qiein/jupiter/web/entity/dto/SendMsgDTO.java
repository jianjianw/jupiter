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
    //手机号码
    private String phone;
    //需要短信发送的变量 是具体而定
    private Map<String,String> map;



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
