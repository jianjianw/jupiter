package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.BlackListPO;

import java.util.List;

/**
 * 黑名单
 * @author: xiangliang
 */
public interface ClientBlackListService {
    /**
     * 删除
     */
    void delete(String ids);
    /**
     * 查找
     */
    List<BlackListPO> select(Integer companyId);
    /**
     * 添加
     */
    void insert(BlackListPO blackListPO);
    /**
     * 修改
     */
    void update(BlackListPO blackListPO);
}
