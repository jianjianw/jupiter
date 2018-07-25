package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.CashLogDao;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private CashLogDao cashLogDao;

    /**
     * 修改付款金额
     *
     * @param cashDTO
     */
    public void editCash(CashLogPO cashLogPO) {
        //修改原本付款记录的状态
        cashLogDao.editStatus(DBSplitUtil.getCashTabName(cashLogPO.getCompanyId()), cashLogPO.getId());
        //新增修改完成的付款记录
        cashLogDao.addCahsLog(DBSplitUtil.getCashTabName(cashLogPO.getCompanyId()), cashLogPO);


    }
}
