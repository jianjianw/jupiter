package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.CashDao;
import com.qiein.jupiter.web.entity.dto.CashDTO;
import com.qiein.jupiter.web.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private CashDao cashDao;
    /**
     * 修改付款金额
     * @param cashDTO
     */
    public void editCash(CashDTO cashDTO){
        cashDTO.setTable(DBSplitUtil.getTable(TableEnum.cash_log,cashDTO.getCompanyId()));

        //修改原本付款记录的状态
        cashDao.editStatus(cashDTO.getTable(),cashDTO.getId());
        //新增修改完成的付款记录
        cashDao.insert(cashDTO);


    }
}
