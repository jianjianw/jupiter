package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.vo.CashLogVO;

import java.util.List;

public interface CashService {
    /**
     * 修改付款金额
     *
     * @param cashLogPO
     */
    int editCash(CashLogPO cashLogPO);

    /**
     * 添加收款记录
     *
     * @param cashLogPO
     */
    int addCashLog(CashLogPO cashLogPO);

    /**
     * 付款记录查询页面
     *
     * @param kzId
     * @return
     */
    List<CashLogVO> findCashLog(String kzId);
}
