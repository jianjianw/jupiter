package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
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
        String cashTableName = DBSplitUtil.getCashTabName(companyId);
        String infoTableName = DBSplitUtil.getInfoTabName(companyId);
        String infoLogTableName = DBSplitUtil.getInfoLogTabName(companyId);
        String detailTableName = DBSplitUtil.getDetailTabName(companyId);
        String kzId = cashLogPO.getKzId();
        //查询旧记录，用于生产修改记录
        CashLogPO oldCash = cashLogDao.getCashLogById(cashTableName, cashLogPO.getId(), companyId);
        //修改已收金额
        cashLogDao.editAmount(cashTableName, cashLogPO.getAmount(), cashLogPO.getId(), companyId, cashLogPO.getStaffId(), cashLogPO.getStaffName(), cashLogPO.getPayStyle(), cashLogPO.getPaymentTime());
        //添加修改日志
        clientLogDao.addInfoLog(infoLogTableName, new
                ClientLogPO(kzId, cashLogPO.getOperaId(),
                cashLogPO.getOperaName(), ClientLogConst.getCashEditLog(cashLogPO, oldCash),
                ClientLogConst.INFO_LOGTYPE_CASH, companyId));
        //修改已收金额
        clientInfoDao.editStayAmount(detailTableName, cashTableName,
                kzId, cashLogPO.getCompanyId());
        ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId, infoTableName, detailTableName);
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
        int companyId = cashLogPO.getCompanyId();
        String kzId = cashLogPO.getKzId();
        String cashTableName = DBSplitUtil.getCashTabName(companyId);
        String infoTableName = DBSplitUtil.getInfoTabName(companyId);
        String detailTableName = DBSplitUtil.getDetailTabName(companyId);
        if (NumUtil.isInValid(cashLogPO.getTypeId())) {
            cashLogPO.setTypeId(CommonConstant.CASH_TYPE_TAIL);
        }
        //添加收款记录
        cashLogDao.addCahsLog(cashTableName, cashLogPO);
        //修改已收金额
        clientInfoDao.editStayAmount(DBSplitUtil.getDetailTabName(cashLogPO.getCompanyId()), cashTableName,
                kzId, companyId);
        ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId, infoTableName, detailTableName);
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
    public List<CashLogVO> findCashLog(String kzId, String table) {
        return cashLogDao.findCashLog(kzId, table);
    }

    /**
     * 删除付款记录
     *
     * @param id
     */
    public void deleteCashLog(Integer id, String kzId, Integer companyId, Integer staffId, String staffName) {
        String detailTableName = DBSplitUtil.getDetailTabName(companyId);
        String cashTableName = DBSplitUtil.getCashTabName(companyId);
        String infoLogTableName = DBSplitUtil.getInfoLogTabName(companyId);
        CashLogPO oldCash = cashLogDao.getCashLogById(cashTableName, id, companyId);
        cashLogDao.deleteCashLog(id);
        //添加修改日志
        clientLogDao.addInfoLog(infoLogTableName, new
                ClientLogPO(kzId, staffId,
                staffName, ClientLogConst.getCashDeleteLog(oldCash, staffName),
                ClientLogConst.INFO_LOGTYPE_CASH, companyId));
        clientInfoDao.editStayAmount(detailTableName, cashTableName,
                kzId, companyId);
    }

}
