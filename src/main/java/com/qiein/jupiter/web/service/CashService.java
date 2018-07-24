package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CashLogPO;

public interface CashService {
    /**
     * 修改付款金额
     *
     * @param cashLogPO
     */
    void editCash(CashLogPO cashLogPO);
}
