package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.StatusPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客资状态管理dao
 */
public interface StatusDao extends BaseDao<StatusPO> {
	/**
	 * 获取企业状态列表
	 *
	 * @param companyId
	 * @return
	 */
	List<StatusPO> getCompanyStatusList(@Param("companyId") int companyId);

	/**
	 * 编辑状态
	 *
	 * @param statusPO
	 * @return
	 */
	int editStatus(StatusPO statusPO);

	/**
	 * 获取状态信息
	 * 
	 * @return
	 */
	StatusPO getStatusById(@Param("companyId") int companyId, @Param("statusId") int statusId);
}
