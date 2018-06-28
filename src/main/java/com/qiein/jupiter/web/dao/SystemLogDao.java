package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.SystemLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统日志
 *
 * @author JingChenglong 2018/05/14 15:56
 */
public interface SystemLogDao extends BaseDao<SystemLogDao> {

    /**
     * 新增操作日志
     *
     * @param log
     */
    void addSystemLog(@Param("log") SystemLog log);

    /**
     * 查询员工登录ip是否存在
     *
     * @param companyId
     * @param staffId
     * @param ip
     * @return
     */
    int getCountByStaffIdAndIp(@Param("companyId") int companyId, @Param("staffId") int staffId, @Param("ip") String ip);

    /**
     * 根据类型查询公司的日志
     *
     * @param companyId
     * @param typeId
     * @return
     */
    List<SystemLog> getLogByType(@Param("companyId") int companyId, @Param("typeId") int typeId);

    /**
     * 清空多久之前的日志
     *
     * @param time
     */
    int clearLog(int time);
}