package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.AllotLogPO;

/**
 * 客资分配日志
 * 
 * @author JingChenglong 2018/05/09 11:49
 *
 */
public interface ClientAllotLogDao extends BaseDao<ClientAllotLogDao> {

	/***
	 * 新增分配日志
	 * 
	 * @param allotLog
	 * @return
	 */
	public void addClientAllogLog(@Param("allotLogTabName") String allotLogTabName,
			@Param("allotLog") AllotLogPO allotLog);

	/**
	 * 修改客资分配日志
	 * 
	 * @param allotLogTabName
	 * @param companyId
	 * @param statusId
	 * @param receiveFlag
	 * @return
	 */
	public int updateAllogLog(@Param("allotLogTabName") String allotLogTabName, @Param("companyId") int companyId,
			@Param("kzId") String kzId, @Param("logId") int logId, @Param("statusId") int statusId,
			@Param("receiveFlag") String receiveFlag);
}