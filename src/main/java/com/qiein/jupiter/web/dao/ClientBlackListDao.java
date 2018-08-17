package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.BlackListPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 转介绍月底客资报表
 * author xiangliang
 */
@Repository
public interface ClientBlackListDao {
    /**
     * 查找
     */
    List<BlackListPO> select(@Param("companyId") Integer companyId);
    /**
     * 删除
     */
    void delete(@Param("ids")String ids);
    /**
     * 添加
     */
     void insert(BlackListPO blackListPO);
    /**
     * 添加
     */
    void update(BlackListPO blackListPO);
}
