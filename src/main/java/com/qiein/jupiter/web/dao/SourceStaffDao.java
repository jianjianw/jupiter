package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.SourceStaffPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FileName: SourceStaffDao
 *
 * @author: yyx
 * @Date: 2018-6-23 18:52
 */
public interface SourceStaffDao extends BaseDao<SourceStaffPO> {
    /**
     * 根据channelId删除关联
     *
     * @param channelId
     * @param companyId
     */
    void deleteByChannelId(@Param(value = "channelId") int channelId, @Param(value = "companyId") Integer companyId);

    /**
     * 根据channelId插入关联信息
     *
     * @param channelId
     * @param companyId
     * @param yyIds
     * @param relaType
     */
    void insertByChannelId(@Param(value = "channelId") int channelId, @Param(value = "companyId") Integer companyId, @Param("yyIds") List<String> yyIds, @Param(value = "relaType") String relaType);

    /**
     * 根据来源id清除关联信息
     *
     * @param sourceId
     * @param companyId
     */
    void deleteBySourceId(@Param(value = "sourceId") int sourceId, @Param(value = "companyId") Integer companyId);

    /**
     * 根据来源id插入关联信息
     *
     * @param sourceId
     * @param companyId
     * @param yyIds
     * @param relaType
     */
    void insertBySourceId(@Param(value = "sourceId") int sourceId, @Param(value = "companyId") Integer companyId, @Param("yyIds") List<String> yyIds, @Param(value = "relaType") String relaType);
}
