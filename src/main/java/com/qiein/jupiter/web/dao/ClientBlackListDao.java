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

    /**
     * 检测手机号是否重复
     * @param companyId
     * @param phone
     * @return
     */
    List<BlackListPO> getByPhone(@Param("companyId")Integer companyId,@Param("phone")String phone);
    /**
     * 检测是否拦截
     */
    List<BlackListPO> checkBlackList(@Param("companyId")Integer companyId,@Param("kzPhone")String kzPhone,@Param("kzWw")String kzWw,@Param("kzQq")String kzQq,@Param("kzWeChat")String kzWeChat);

    /**
     * 增加拦截次数
     * @param ids
     */
    void addCount(@Param("ids")String  ids);
    }
