package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.vo.ChannelDictVO;
import com.qiein.jupiter.web.entity.vo.ChannelVO;

import java.util.List;
import java.util.Map;

/**
 * 渠道Service
 */
public interface ChannelService {
    /**
     * 新建渠道
     *
     * @param channelPO
     */
    void createChannel(ChannelPO channelPO);

    /**
     * 编辑渠道
     *
     * @param channelPO
     */
    void editChannel(ChannelPO channelPO);

    /**
     * 修改排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @param companyId
     */
    void editProiority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId);

    /**
     * 删除渠道
     *
     * @param id        渠道编号
     * @param companyId 所属公司编号
     */
    void delChannel(Integer id, Integer companyId);

    /**
     * 获取渠道列表
     *
     * @param typeIds
     * @param companyId
     * @return
     */
    List<ChannelPO> getChannelList(List<Integer> typeIds, Integer companyId);

    /**
     * 根据渠道细分类型获取渠道
     * 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
     *
     * @param typeId
     * @param companyId
     * @return
     */
    List<ChannelPO> getListByType(Integer typeId, Integer companyId);

    /**
     * 获取企业各角色页面，头部渠道组及渠道下拉框筛选
     *
     * @param companyId
     * @param role
     * @return
     */
    List<ChannelVO> getChannelSourceListByType(Integer companyId, String role);

    /**
     * 根据公司ID 获取渠道字典
     *
     * @return
     */
    Map<String, ChannelDictVO> getChannelDict(int companyId);
}
