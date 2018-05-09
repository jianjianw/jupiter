package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.ClientLogPO;

/**
 * 客资日志
 * 
 * @author JingChenglong 2018/05/09 17:35
 *
 */
public interface ClientLogDao extends BaseDao<ClientLogDao> {

	/**
	 * 新增客资日志
	 * 
	 * @param logTabName
	 * @param clientLogPO
	 * @return
	 */
	public int addInfoLog(@Param("logTabName") String logTabName, @Param("log") ClientLogPO clientLogPO);
}