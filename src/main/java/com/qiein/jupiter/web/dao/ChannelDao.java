package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.ChannelVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 渠道dao
 * Created by 叶华葳 on 2018/4/19 0019.
 */
public interface ChannelDao extends BaseDao<ChannelPO> {

    /**
     * 检查所属公司下是否有重复渠道， 返回值为同名的渠道组数， 大于0则说明存在多个同名渠道， 小于0则说明不存在同名渠道
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
    List<ChannelPO> getChannelListByTypeIds(@Param("companyId") Integer companyId, @Param("typeIds") List<Integer> typeIds);

    /**
     * 根据id查找
     *
     * @return
     */
    ChannelPO findById(@Param("id") Integer id);

    /**
     * 根据批量渠道名称获取渠道名称列表
     *
     * @param companyId
     * @param ids
     * @return
     */
    String getChannelNamesByIds(@Param("companyId") Integer companyId, @Param("ids") String[] ids);

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
     * 获取各角色渠道组及渠道，只显示开启的渠道来源
     *
     * @param companyId
     * @param typeList
     * @return
     */
    List<ChannelVO> getChannelSourceListByType(@Param("companyId") Integer companyId,
                                               @Param("typeList") List<Integer> typeList);

    /**
     * 获取各角色渠道组及渠道，包含关闭的渠道来源
     *
     * @param companyId
     * @param typeList
     * @return
     */
    List<ChannelVO> getAllSourceListByType(@Param("companyId") Integer companyId, @Param("typeList") List<Integer> typeList);

    /**
     * 根据ID获取显示可用的渠道
     *
     * @param companyId
     * @param id
     * @return
     */
    ChannelPO getShowChannelById(@Param("companyId") int companyId, @Param("id") int id);

    /**
     * 获取员工7天内录入使用渠道的次数排序
     *
     * @param tableName
     * @param companyId
     * @param staffId
     * @param typeList
     * @return
     */
    List<ChannelVO> getHistoryChannelSort(@Param("tableName") String tableName, @Param("companyId") Integer companyId,
                                          @Param("staffId") int staffId, @Param("typeList") List<Integer> typeList);

    /**
     * 根据公司ID 获取所有的渠道
     *
     * @param companyId
     * @return
     */
    List<ChannelPO> getChannelListByCid(@Param("companyId") int companyId);

    /**
     * 根据组名称获取渠道
     *
     * @param groupName
     * @return
     */
    ChannelPO getChannelByGroupName(@Param("groupName") String groupName, @Param("companyId") Integer companyId);

    /**
     * 根据GroupParentId获取渠道
     *
     * @param parentId
     * @param companyId
     * @return
     */
    ChannelPO getChannelByGroupParentId(@Param(value = "parentId") String parentId, @Param(value = "companyId") Integer companyId);

    /**
     * 根据员工id获取员工所在小组的承接渠道列表
     *
     * @param companyId
     * @param groupId
     * @return
     */
    List<ChannelPO> getChannelListByStaffGroup(@Param("companyId") int companyId, @Param("groupId") String groupId);

    /**
     * 获取企业所有渠道组及渠道
     *
     * @param companyId
     * @return
     */
    List<ChannelVO> getAllChannelSourceList(@Param("companyId") int companyId);

    /**
     * 获取电商所有渠道来源
     *
     * @param companyId
     * @return
     */
    List<ChannelVO> getDsAllChannel(int companyId);

    /**
     * 获取企业所有启用渠道及来源列表
     *
     * @param companyId
     * @param channelId
     * @return
     */
    List<ChannelVO> getAllShowChannelSourceList(@Param("companyId") int companyId, @Param("channelId") Integer channelId);
}
