package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.SourcePO;

import java.util.List;

/**
 * 来源Service
 */
public interface SourceService {
    /**
     * 新增来源
     *
     * @param sourcePO
     */
    void createSource(SourcePO sourcePO);

    /**
     * 编辑来源
     *
     * @param sourcePO
     */
    void editSource(SourcePO sourcePO);

    /**
     * 根据来源编号删除来源
     *
     * @param id
     * @param companyId
     */
    void delSourceById(Integer id, Integer companyId);

    /**
     * 根据渠道编号获取归属来源列表
     *
     * @param channelId
     * @param companyId
     * @return
     */
    List<SourcePO> getSourceListByChannelId(Integer channelId, Integer companyId);
}
