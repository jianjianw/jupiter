package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.SourceDictVO;
import com.qiein.jupiter.web.entity.vo.SourceVO;

import java.util.List;
import java.util.Map;

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
     * @param sourceVO
     */
    void editSource(SourceVO sourceVO);

    /**
     * 拖拽排序来源
     *
     * @param fPriority
     * @param sPriority
     * @param id
     * @param companyId
     */
    void editSourcePriority(Integer fPriority, Integer sPriority, Integer id, Integer companyId);

    /**
     * 根据来源编号删除来源
     *
     * @param id
     * @param companyId
     */
    void delSourceById(Integer id, Integer companyId);

    /**
     * 根据来源编号批量删除来源
     *
     * @param ids
     * @param companyId
     */
    void datDelSrc(String ids, Integer companyId);

    /**
     * 根据渠道编号获取归属来源列表
     *
     * @param channelId
     * @param companyId
     * @return
     */
    List<SourcePO> getSourceListByChannelId(Integer channelId, Integer companyId);

    /**
     * 获取公司的渠道字典
     *
     * @param companyId
     * @return
     */
    Map<String, SourceDictVO> getSourcePageMap(int companyId);
}
