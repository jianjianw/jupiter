package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.ClientDao;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tt on 2018/5/15 0015.
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ClientLogDao clientLogDao;

    /**
     * 编辑客资性别
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientSex(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(),clientStatusVO.getOperaId(),clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_SEX+(clientStatusVO.getSex()==1?"先生":"女士"),ClientLogConst.INFO_LOGTYPE_EDIT,clientStatusVO.getCompanyId()));
        if (addLogNum!=1){
            System.out.println("插入客资日志失败");
        }
    }

    /**
     * 编辑客资微信标识
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientWCFlag(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(),clientStatusVO.getOperaId(),clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_WCFLAG+(clientStatusVO.getWeFlag()==1?"已添加":"没加上"),ClientLogConst.INFO_LOGTYPE_EDIT,clientStatusVO.getCompanyId()));
        if (addLogNum!=1){
            System.out.println("插入客资日志失败");
        }
    }
}
