package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
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
        List<BlackListPO> list=clientBlackListDao.getByPhone(blackListPO.getCompanyId(),blackListPO.getKzPhone());
        if(list.size()==0){
            throw new RException(ExceptionEnum.BLACK_WAS_IN);
        }
        clientBlackListDao.insert(blackListPO);
    }
    /**
     * 添加
     */
    public void update(BlackListPO blackListPO){
        clientBlackListDao.update(blackListPO);
    }
}
