package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CommonTypePO;
import com.qiein.jupiter.web.entity.vo.CommonTypeChannelVO;
import com.qiein.jupiter.web.entity.vo.CommonTypeVO;
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
    List<CommonTypeChannelVO> findChannelGroup(@Param("typeId")Integer typeId, @Param("companyId")Integer companyId);

    /**
     * 获取拍摄类型
     * @param companyId
     * @return
     */
    List<CommonTypeVO> findCommonType(@Param("companyId")Integer companyId);

    /**
     * 获取来源信息
     * @param typeId
     * @param companyId
     * @return
     */
    List<CommonTypeChannelVO> findChannel(@Param("typeId")Integer typeId, @Param("companyId")Integer companyId);
    /**
     * 根据来源id删除
     * @param channelId
     * @param typeId
     * @param companyId
     */
    void deleteByChannelId( @Param("channelId")Integer channelId,@Param("typeId")Integer typeId, @Param("companyId")Integer companyId);
    /**
     * 搜索小组
     * @param channelId
     * @param typeId
     * @param companyId
     * @param groupName
     * @return
     */
    List<CommonTypePO> searchByChannelId(@Param("channelId")Integer channelId,@Param("typeId")Integer typeId,@Param("companyId")Integer companyId,@Param("groupName")String groupName);

    /**
     * 批量修改
     * @param list
     * @param weight
     * @param typeId
     * @param channelId
     */
    void editWeight(@Param("list")List<String> list,@Param("typeId")Integer typeId,@Param("channelId")Integer channelId,@Param("weight")Integer weight );
}
