package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.SystemLog;

/**
 * 系统日志
 * 
 * @author JingChenglong 2018/05/14 16:24
 *
 */
public interface SystemLogService {

	/**
	 * 部门新增
	 *
	 * @param groupPO
	 */
	void addLog(SystemLog log);
}