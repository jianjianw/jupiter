package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;
import com.qiein.jupiter.web.entity.po.SystemLog;

/**
 * 系统日志
 * 
 * @author JingChenglong 2018/05/14 15:56
 *
 */
public interface SystemLogDao extends BaseDao<SystemLogDao> {

	/**
	 * 新增操作日志
	 * 
	 * @param logTabName
	 * @param log
	 */
	void addSystemLog(@Param("logTabName") String logTabName, @Param("log") SystemLog log);
}