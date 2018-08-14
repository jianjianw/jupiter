package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.OnLineTimePO;
import org.apache.ibatis.annotations.Param;

/**
 * 在线时长日志记录Dao
 *
 * @author gaoxiaoli 2018/6/2
 */

public interface OnLineTimeDao extends BaseDao<OnLineTimePO> {
    /**
     * 添加在线时长日志
     *
     * @param staffId
     * @param companyId
     * @param staffName
     */
    void addOnLineTimeLog(@Param("staffId") int staffId, @Param("companyId") int companyId, @Param("staffName") String staffName);

    /**
     * 修改在线时长
     *
     * @param staffId
     * @param companyId
     * @param time
     */
    void updateOnLineTime(@Param("staffId") int staffId, @Param("companyId") int companyId, @Param("time") int time);

    /**
     * 根据员工ID和日期，查询日志
     *
     * @param staffId
     * @param companyId
     * @return
     */
    OnLineTimePO getLogByStaffAndDay(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 获取上一次心跳时间
     *
     * @param staffId
     * @param companyId
     * @return
     */
    int getLastHeartTime(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 更新上次心跳时间
     *
     * @param staffId
     * @param companyId
     */
    void updateLastHeartTime(@Param("staffId") int staffId, @Param("companyId") int companyId);
}
