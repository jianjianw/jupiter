package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

public interface ClientAddService {

    /**
     * 添加电商客资
     *
     * @param clientVO
     * @param staffPO
     */
    public void addDsClient(ClientVO clientVO, StaffPO staffPO);
}
