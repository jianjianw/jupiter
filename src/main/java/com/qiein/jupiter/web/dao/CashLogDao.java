package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import org.apache.ibatis.annotations.Param;

/**
 * 收款日志
 *
 * @author gaoxiaoli 2018/05/09 17:35
 */
public interface CashLogDao extends BaseDao<CashLogDao> {

    /**
     * 新增客资日志
     *
     * @param logTabName
     * @param cashLogPO
     * @return
     */
    int addCahsLog(@Param("logTabName") String logTabName, @Param("log") CashLogPO cashLogPO);
}