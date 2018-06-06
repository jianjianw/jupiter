package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;
import com.qiein.jupiter.web.entity.po.SystemLog;

/**
 * 系统日志
 *
 * @author JingChenglong 2018/05/14 15:56
 */
public interface SystemLogDao extends BaseDao<SystemLogDao> {

    /**
     * 新增操作日志
     *
     * @param logTabName
     * @param log
     */
    void addSystemLog(@Param("logTabName") String logTabName, @Param("log") SystemLog log);

    /**
     * 查询员工登录ip是否存在
     *
     * @param logTabName
     * @param companyId
     * @param staffId
     * @param ip
     * @return
     */
    int getCountByStaffIdAndIp(@Param("logTabName") String logTabName, @Param("companyId") int companyId, @Param("staffId") int staffId, @Param("ip") String ip);
}