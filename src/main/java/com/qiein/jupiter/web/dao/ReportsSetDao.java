package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ReportsSetPO;
import org.apache.ibatis.annotations.Param;

/**
 * 报表设置
 *
 * @Author: shiTao
 */
public interface ReportsSetDao extends BaseDao<ReportsSetPO> {
    /**
     * 获取报表定义
     *
     * @return
     */
    String getDefineSet(int companyId);

    /**
     * 修改报表定义
     */
    int updateDefineSet(@Param("companyId") int companyId, @Param("set") String set);

    /**
     * 获取电商推广 来源数据统计 表头
     *
     * @return
     */
    String getR1ShowTitleSet(int companyId);

    /**
     * 修改电商推广 来源数据统计 表头
     */
    int updateR1ShowTitleSet(@Param("companyId") int companyId, @Param("set") String set);
}
