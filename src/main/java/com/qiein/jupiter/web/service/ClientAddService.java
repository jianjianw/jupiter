package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONArray;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

public interface ClientAddService {

    /**
     * 添加电商客资
     *
     * @param clientVO
     * @param staffPO
     */
    void addDsClient(ClientVO clientVO, StaffPO staffPO);

    /**
     * 批量录入信息解析
     *
     * @param text
     */
    JSONArray changeStrToInfo(String text);
}
