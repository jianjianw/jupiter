package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.CashLogDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.vo.CashLogVO;
import com.qiein.jupiter.web.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private CashLogDao cashLogDao;
    @Autowired
    private ClientInfoDao clientInfoDao;

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
        //修改已收金额
        clientInfoDao.editStayAmount(DBSplitUtil.getDetailTabName(cashLogPO.getCompanyId()), DBSplitUtil.getCashTabName(cashLogPO.getCompanyId()), cashLogPO.getKzId(), cashLogPO.getCompanyId());

    }


    /**
     * 添加收款记录
     *
     * @param cashLogPO
     */
    public void addCashLog(CashLogPO cashLogPO) {
        //添加收款记录
        cashLogDao.addCahsLog(DBSplitUtil.getCashTabName(cashLogPO.getCompanyId()), cashLogPO);
        //修改已收金额
        clientInfoDao.editStayAmount(DBSplitUtil.getDetailTabName(cashLogPO.getCompanyId()), DBSplitUtil.getCashTabName(cashLogPO.getCompanyId()), cashLogPO.getKzId(), cashLogPO.getCompanyId());
    }
    /**
     * 付款记录查询页面
     * @param kzId
     * @param table
     * @return
     */
    public List<CashLogVO> findCashLog(String kzId, String table){
        return cashLogDao.findCashLog(kzId,table);
    }

}
