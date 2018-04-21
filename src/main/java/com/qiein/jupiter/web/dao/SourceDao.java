package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.SourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 来源Dao
 */
public interface SourceDao extends BaseDao<SourcePO>{
    /**
     * 来源同名校验，检查同一渠道下是否存在同名来源，存在返回大于等于1，不存在返回0
     * @param srcName
     * @param companyId
     * @return
     */
    Integer checkSource(@Param("srcName") String srcName,@Param("channelId") Integer channelId, @Param("companyId") Integer companyId);

    /**
     * 根据渠道编号获取旗下的来源列表
     * @param channelId 渠道编号
     * @param companyId 所属公司编号
     * @return
     */
    List<SourcePO> getSourceListByChannelId(@Param("channelId")Integer channelId ,@Param("companyId") Integer companyId);

    /**
     * 批量编辑来源
     * @param sourceVO
     * @param ids
     * @return
     */
    Integer datUpdate(@Param("sv") SourceVO sourceVO ,@Param("ids") String[] ids);

    /**
     * 批量删除来源
     * @param ids
     * @param companyId
     * @return
     */
    Integer datDelete(@Param("ids")String[] ids ,@Param("companyId") Integer companyId );
}
