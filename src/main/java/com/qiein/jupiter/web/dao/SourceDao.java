package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.SourceVO;
import org.apache.ibatis.annotations.Param;

import javax.ws.rs.POST;
import java.util.List;

/**
 * 来源Dao
 */
public interface SourceDao extends BaseDao<SourcePO> {
    /**
     * 来源同名校验，检查同一渠道下是否存在同名来源，存在返回大于等于1，不存在返回0
     *
     * @param srcName
     * @param companyId
     * @return
     */
    Integer checkSource(@Param("srcName") String srcName, @Param("channelId") Integer channelId,
                        @Param("companyId") Integer companyId);

    /**
     * 根据渠道编号获取旗下的来源列表
     *
     * @param channelId 渠道编号
     * @param companyId 所属公司编号
     * @return
     */
    List<SourcePO> getSourceListByChannelId(@Param("channelId") Integer channelId, @Param("companyId") Integer companyId);

    /**
     * 批量编辑来源
     *
     * @param sourceVO
     * @param ids
     * @return
     */
    Integer datUpdate(@Param("sv") SourceVO sourceVO, @Param("ids") String[] ids);

    /**
     * 批量删除来源
     *
     * @param ids
     * @param companyId
     * @return
     */
    Integer datDelete(@Param("ids") String[] ids, @Param("companyId") Integer companyId);

    /**
     * 查询所选要删除的渠道下存在多少客资
     *
     * @param ids
     * @return
     */
    Integer datDelCheck(@Param("ids") String[] ids);

    /**
     * 渠道名变更时下属所有来源的记录也要相应变更
     *
     * @param channelId
     * @param channelName
     * @param companyId
     * @return
     */
    Integer updateChannelName(@Param("channelId") Integer channelId, @Param("channelName") String channelName,
                              @Param("companyId") Integer companyId);

    /**
     * 渠道关闭时，同时关闭下属所有来源
     *
     * @param channelId
     * @param companyId
     * @return
     */
    Integer updateIsShowByChannelId(@Param("channelId") Integer channelId, @Param("companyId") Integer companyId);

    /**
     * 渠道关闭时，同时关闭下属所有来源
     *
     * @param channelId
     * @param companyId
     * @return
     */
    Integer updateIsFilterByChannelId(@Param("channelId") Integer channelId, @Param("companyId") Integer companyId, @Param("flag") boolean flag);

    /**
     * 修改向下拖拽排序时波及的来源的排序
     *
     * @param xPriority
     * @param dPriority
     * @param companyId
     * @return
     */
    Integer updateDownPriority(@Param("xPriority") Integer xPriority, @Param("dPriority") Integer dPriority,
                               @Param("companyId") Integer companyId);

    /**
     * 修改向上拖拽排序时波及的来源的排序
     *
     * @param xPriority 小的排序号
     * @param dPriority 大的排序号
     * @param companyId
     * @return
     */
    Integer updateUpPriority(@Param("xPriority") Integer xPriority, @Param("dPriority") Integer dPriority,
                             @Param("companyId") Integer companyId);

    /**
     * 根据来源ID获取显示可用的来源信息
     *
     * @param companyId
     * @param id
     * @return
     */
    SourcePO getShowSourceById(@Param("companyId") Integer companyId, @Param("id") int id);

    /**
     * 获取员工上个月录入使用来源的次数排序
     *
     * @param tableName
     * @param companyId
     * @param staffId
     * @param typeList
     * @return
     */
    List<Integer> getLastMonthSrcSort(@Param("tableName") String tableName, @Param("companyId") Integer companyId,
                                      @Param("staffId") int staffId, @Param("typeList") List<Integer> typeList);


    /**
     * 获取公司下所有的来源信息，包括删除的
     *
     * @param companyId
     * @return
     */
    List<SourcePO> getAllSourceListByCid(int companyId);

    /**
     * 根据渠道id，来源名称获取来源
     *
     * @param srcName
     * @param companyId
     * @param channelId
     * @return
     */
    SourcePO getSourceBySrcname(@Param(value = "srcName") String srcName, @Param(value = "companyId") Integer companyId, @Param(value = "channelId") Integer channelId);

    /**
     * 根据名称获取来源
     * @param srcName
     * @param companyId
     * @return
     * */
    SourcePO getSourceByNameAndType(@Param(value="srcName") String srcName,@Param(value="companyId") int companyId,@Param(value="type")Integer type);

    /**
     * 根据渠道id更改来源pushRule
     *
     * @param channelId
     * @param companyId
     * @param pushRule
     */
    void updatePushRuleByChannelId(@Param(value = "channelId") int channelId, @Param(value = "companyId") Integer companyId, @Param(value = "pushRule") Integer pushRule);

    /**
     * 根据来源名，类型获取渠道
     *
     * @param companyId
     * @param typeId
     * @return
     */
    SourcePO getSourceByType(@Param("companyId") int companyId, @Param("typeId") int typeId, @Param("srcName") String srcName);

    /**
     * 查询所属公司所有显示的渠道
     * @param companyId
     * @return
     */
    String getAllZjsSourceIdStr(@Param("companyId")int companyId);

    /**
     * 根据typeid查询渠道
     * @param companyId
     * @param typeId
     * @return
     */
    List<SourcePO> findSourseByType(@Param("companyId") int companyId,@Param("typeId")String typeId);

}
