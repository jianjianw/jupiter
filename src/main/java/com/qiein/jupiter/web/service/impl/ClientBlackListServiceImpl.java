package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ClientBlackListDao;
import com.qiein.jupiter.web.entity.po.BlackListPO;
import com.qiein.jupiter.web.service.ClientBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 黑名单
 * @author: xiangliang
 */
@Service
public class ClientBlackListServiceImpl implements ClientBlackListService {
    @Autowired
    private ClientBlackListDao clientBlackListDao;

    /**
     * 删除
     */
    public void delete(String ids){
        clientBlackListDao.delete(ids);
    }
    /**
     * 查找
     */
    public List<BlackListPO> select(Integer companyId){
        return clientBlackListDao.select(companyId);
    }
    /**
     * 添加
     */
    public void insert(BlackListPO blackListPO){
        clientBlackListDao.insert(blackListPO);
    }
    /**
     * 添加
     */
    public void update(BlackListPO blackListPO){
        clientBlackListDao.update(blackListPO);
    }
}
