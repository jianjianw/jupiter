package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ClientDao;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDao clientDao;

    /**
     * 编辑客资性别
     * @param clientVO
     */
    @Override
    public void editClientSex(ClientVO clientVO) {
        clientDao.editClientBaseInfo(clientVO);
        //TODO 记录到客资日志中
    }

    /**
     * 编辑客资微信标识
     * @param clientVO
     */
    @Override
    public void editClientWCFlag(ClientVO clientVO) {
        clientDao.editClientBaseInfo(clientVO);
        //TODO 记录到客资日志中
    }
}
