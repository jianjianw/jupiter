package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.dto.GroupKzNumToday;

/**
 * 客资领取情况
 *
 * @author JingChenglong 2018/05/08 16:00
 */
public interface GroupKzNumTodayDao extends BaseDao<GroupKzNumTodayDao> {

    /**
     * 查询指定渠道和拍摄类型当天各客服组客资分配情况
     *
     * @param typeId
     * @param channelId
     * @param companyId
     * @param infoTabName
     * @return
     */
    List<GroupKzNumToday> getGroupKzNumTodayByShopChannelId(@Param("typeId") int typeId,
                                                            @Param("channelId") int channelId, @Param("companyId") int companyId,
                                                            @Param("infoTabName") String infoTabName);
}