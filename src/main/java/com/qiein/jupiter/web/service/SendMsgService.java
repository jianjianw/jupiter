package com.qiein.jupiter.web.service;

public interface SendMsgService {

    /**
     * 获取模板id
     * @param type
     * @param companyId
     * @return
     */
    String getTemplateId(String type,Integer companyId);
}
