package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 拍摄类型渠道
 * Author xiangliang 2018/6/15
 */
public interface CommonTypeSerivce {
    /**
     * 添加拍摄类型
     * @param commonType
     * @param companyId
     */
    void addPhotoType(String commonType,Integer companyId);

    /**
     * 为每个小组分配渠道
     * @param commonTypePO
     */
    void addTypeChannelGroup( CommonTypePO commonTypePO);

    /**
     * 批量删除
     */
    void deleteTypeChannelGroup(String ids);
    /**
     * 修改渠道
     * @param commonTypePO
     */
    void editTypeChannelGroup( CommonTypePO commonTypePO);

    /**
     * 获取拍摄地渠道小组分类
     * @param typeId
     * @return
     */
    List<CommonTypePO> findChannelGroup(Integer typeId, Integer companyId);

    /**
     * 第一次进入时获取拍摄地渠道小组分类
     * @param companyId
     * @return
     */
    CommonTypeChannelVO findChannelGroupFirst(Integer companyId);
}
