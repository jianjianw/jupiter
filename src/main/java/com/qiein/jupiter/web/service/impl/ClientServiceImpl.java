package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ClientDao;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tt on 2018/5/15 0015.
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDao clientDao;

    /**
     * 编辑客资性别
     * @param clientStatusVO
     */
    @Override
    public void editClientSex(ClientStatusVO clientStatusVO) {
        //TODO 改用枚举获取分表名称
        clientDao.editClientBaseInfo(clientStatusVO,"hm_crm_client_info_"+clientStatusVO.getCompanyId());
        //TODO 记录到客资日志中
    }

    /**
     * 编辑客资微信标识
     * @param clientStatusVO
     */
    @Override
    public void editClientWCFlag(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO,"hm_crm_client_info_"+clientStatusVO.getCompanyId());
        //TODO 记录到客资日志中
    }
}
