package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;

/**
 * 客资
 * 
 * @author JingChenglong 2018/05/08 16:00
 *
 */
public interface ClientInfoDao extends BaseDao<ClientInfoDao> {

	/**
	 * 根据名字类型，获取来源信息
	 *
	 * @param companyId
	 * @param srcName
	 * @param typeId
	 * @return
	 */
	public ClientPushDTO getClientPushDTOById(@Param("kzId") String kzId, @Param("infoTabName") String infoTabName);
}