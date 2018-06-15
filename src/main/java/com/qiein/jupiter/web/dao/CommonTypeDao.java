package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CommonTypePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 拍摄类型渠道
 * Author xiangliang 2018/6/15
 */
public interface CommonTypeDao {
    /**
     * 添加拍摄类型
     * @param commonType
     * @param companyId
     */
    void addPhotoType(@Param("commonType")String commonType, @Param("companyId")Integer companyId);
    /**
     * 为每个小组分配渠道
     * @param list
     */
    void addTypeChannelGroup(List<CommonTypePO> list);

    /**
     * 批量删除
     * @param list
     */
    void deleteTypeChannelGroup(List<Integer> list);
}
