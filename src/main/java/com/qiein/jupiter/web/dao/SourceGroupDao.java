package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import org.apache.ibatis.annotations.Param;

/**
 * 渠道组Dao
 */
public interface SourceGroupDao  extends BaseDao<ChannelPO>{

    /**
     * 检查所属公司下是否有重复渠道组，
     * 返回值为同名的渠道组数，
     * 大于0则说明存在多个同名渠道，
     * 小于0则说明不存在同名渠道
     *
     * @param grpName   需要检查的渠道组名
     * @param companyId 所属公司编号
     * @return
     */
    Integer checkSrcGrp(@Param("grpName") String grpName, @Param("companyId") Integer companyId);

}
