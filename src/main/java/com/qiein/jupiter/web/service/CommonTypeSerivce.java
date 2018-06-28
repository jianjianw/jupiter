package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelShowVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeVO;
import org.springframework.web.bind.annotation.RequestParam;

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
    List<CommonTypeChannelVO> findChannelGroup(Integer typeId, Integer companyId);

    /**
     * 第一次进入时获取拍摄地渠道小组分类
     * @param companyId
     * @return
     */
   // CommonTypeChannelShowVO findChannelGroupFirst(Integer companyId);

    /**
     * 根据来源id删除
     * @param channelId
     * @param typeId
     * @param companyId
     */
    void deleteByChannelId(Integer channelId,Integer typeId,Integer companyId);

    /**
     * 搜索小组
     * @param channelId
     * @param typeId
     * @param companyId
     * @param groupName
     * @return
     */
    List<CommonTypePO> searchByChannelId(Integer channelId, Integer typeId, Integer companyId, String groupName);

    /**
     * 查询拍摄类型
     * @param companyId
     * @return
     */
    List<CommonTypeVO> findCommonType(Integer companyId);

    /**
     * 批量修改
     * @param ids
     */
    void editWeight(String ids, Integer weight);

}
