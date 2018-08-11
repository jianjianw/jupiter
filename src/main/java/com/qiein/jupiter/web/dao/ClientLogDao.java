package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.ClientLogPO;

import java.util.List;
import java.util.Map;

/**
 * 客资日志
 *
 * @author JingChenglong 2018/05/09 17:35
 */
public interface ClientLogDao extends BaseDao<ClientLogDao> {

    /**
     * 新增客资日志
     *
     * @param logTabName
     * @param clientLogPO
     * @return
     */
    int addInfoLog(@Param("logTabName") String logTabName, @Param("log") ClientLogPO clientLogPO);

    /**
     * 查询客资收款修改日志
     *
     * @param logTabName
     * @param companyId
     * @param kzId
     * @param logType
     * @return
     */
    List<ClientLogPO> getCashEditLog(@Param("logTabName") String logTabName, @Param("companyId") int companyId,
                                     @Param("kzId") String kzId, @Param("logType") int logType);

    /**
     * 添加邀约记录
     *
     * @param logTabName
     * @param kzId
     * @param yyMemo
     * @param staffId
     * @param companyId
     * @return
     */
    int addInvitationLog(@Param("logTabName") String logTabName, @Param("kzId") String kzId, @Param("yyMemo") String yyMemo,
                         @Param("staffId") int staffId, @Param("companyId") int companyId);
}