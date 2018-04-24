package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.vo.ChannelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道dao
 * Created by Administrator on 2018/4/19 0019.
 */
public interface ChannelDao extends BaseDao<ChannelPO> {
    /**
     * 检查所属公司下是否有重复渠道，
     * 返回值为同名的渠道组数，
     * 大于0则说明存在多个同名渠道，
     * 小于0则说明不存在同名渠道
     *
     * @param channelName 需要检查的渠道名
     * @param companyId   所属公司编号
     * @return
     */
    Integer checkChannel(@Param("channelName") String channelName, @Param("companyId") Integer companyId);

    /**
     * 获取渠道列表
     *
     * @param companyId
     * @param typeIds
     * @return
     */
    List<ChannelPO> getChannelList(@Param("companyId") Integer companyId, @Param("typeIds") List<Integer> typeIds);

    /**
     * 根据细分渠道类型获取渠道信息
     *
     * @param companyId
     * @param typeId
     * @return
     */
    List<ChannelPO> getListByType(@Param("companyId") Integer companyId, @Param("typeId") Integer typeId);

    /**
     * 检查该渠道下属存在多少来源
     *
     * @param channelId
     * @param companyId
     * @return
     */
    Integer checkSrcNumById(@Param("channelId") Integer channelId, @Param("companyId") Integer companyId);

    /**
     * 获取各角色渠道组及渠道
     *
     * @param companyId
     * @param typeList
     * @return
     */
    List<ChannelVO> getChannelSourceListByType(@Param("companyId") Integer companyId, @Param("typeList") List<Integer> typeList);
}
