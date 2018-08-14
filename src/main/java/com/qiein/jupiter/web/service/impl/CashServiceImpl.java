package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.CashLogDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.vo.CashLogVO;
import com.qiein.jupiter.web.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private CashLogDao cashLogDao;
    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private ClientLogDao clientLogDao;

    /**
     * 修改付款金额
     */
    @Transactional
    public int editCash(CashLogPO cashLogPO) {
        int companyId = cashLogPO.getCompanyId();
        String kzId = cashLogPO.getKzId();
        //查询旧记录，用于生产修改记录
        CashLogPO oldCash = cashLogDao.getCashLogById(cashLogPO.getId(), companyId);
        //修改已收金额
        cashLogDao.editAmount(cashLogPO.getAmount(), cashLogPO.getId(), companyId);
        //添加修改日志
        clientLogDao.addInfoLog(new
                ClientLogPO(kzId, cashLogPO.getOperaId(),
                cashLogPO.getOperaName(), ClientLogConst.getCashEditLog(cashLogPO, oldCash),
                ClientLogConst.INFO_LOGTYPE_CASH, companyId));
        //修改已收金额
        clientInfoDao.editStayAmount(kzId, cashLogPO.getCompanyId());
        ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId);
        //判断已收是否大于总额，一旦大于 异常回滚
        if (info.getStayAmount() > info.getAmount()) {
            throw new RException(ExceptionEnum.AMOUNT_ERROR);
        }

        //查询已收金额
        return info.getStayAmount();
    }


    /**
     * 添加收款记录
     *
     * @param cashLogPO
     */
    @Transactional
    public int addCashLog(CashLogPO cashLogPO) {
        //添加收款记录
        cashLogDao.addCahsLog(cashLogPO);
        //修改已收金额
        clientInfoDao.editStayAmount(cashLogPO.getKzId(), cashLogPO.getCompanyId());
        ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(cashLogPO.getKzId());
        //判断已收是否大于总额，一旦大于 异常回滚
        if (info.getStayAmount() > info.getAmount()) {
            throw new RException(ExceptionEnum.AMOUNT_ERROR);
        }
        //查询已收金额
        return info.getStayAmount();
    }

    /**
     * 付款记录查询页面
     *
     * @param kzId
     * @param table
     * @return
     */
    public List<CashLogVO> findCashLog(String kzId) {
        return cashLogDao.findCashLog(kzId);
    }

}
