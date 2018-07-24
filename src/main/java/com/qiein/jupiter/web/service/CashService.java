package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.CashDTO;

public interface CashService {
    /**
     * 修改付款金额
     * @param cashDTO
     */
    void editCash(CashDTO cashDTO);
}
