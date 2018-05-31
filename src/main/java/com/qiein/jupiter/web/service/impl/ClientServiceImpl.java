package com.qiein.jupiter.web.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.ClientDao;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.service.ClientService;

/**
 * Created by Tt on 2018/5/15 0015.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ClientLogDao clientLogDao;

    /**
     * 编辑客资性别
     *
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientSex(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_SEX + (clientStatusVO.getSex() == 1 ? "先生" : "女士"),
                        ClientLogConst.INFO_LOGTYPE_EDIT, clientStatusVO.getCompanyId()));
        if (addLogNum != 1) {
            log.error("插入客资日志失败");
        }
    }

    /**
     * 编辑客资微信标识
     *
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientWCFlag(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_WCFLAG + (clientStatusVO.getWeFlag() == 1 ? "已添加" : "没加上"),
                        ClientLogConst.INFO_LOGTYPE_EDIT, clientStatusVO.getCompanyId()));
        if (addLogNum != 1) {
            log.error("插入客资日志失败");
        }
    }

    /**
     * 微信二位码扫描记录
     *
     * @param companyId
     * @param kzId
     */
    public void scanWechat(int companyId, String kzId) {
        clientDao.editClientMemoLabel(DBSplitUtil.getDetailTabName(companyId), companyId, kzId, "【微信已扫码】");
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId), new ClientLogPO(kzId,
                CommonConstant.DEFAULT_ZERO, null, "通过扫描二维码复制了微信账号", ClientLogConst.INFO_LOGTYPE_EDIT, companyId));
    }

    /**
     * 根据状态筛选客资数量
     *
     * @param companyId
     * @return
     */
    public HashMap<String, Integer> getKzNumByStatusId(int companyId) {
        HashMap<String, Integer> result = new HashMap<>();
        result.put("notAllot", clientDao.getKzNumByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, companyId, DBSplitUtil.getInfoTabName(companyId)));
        result.put("beAlloting", clientDao.getKzNumByStatusId(ClientStatusConst.BE_ALLOTING, companyId, DBSplitUtil.getInfoTabName(companyId)));
        return result;
    }
}
