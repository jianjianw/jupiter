package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StatusPO;

import java.util.List;

/**
 * 状态管理
 */
public interface StatusService {

	/**
	 * 获取企业状态列表
	 *
	 * @param companyId
	 * @return
	 */
	List<StatusPO> getCompanyStatusList(int companyId);

	/**
	 * 编辑状态
	 *
	 * @param statusPO
	 * @return
	 */
	void editStatus(StatusPO statusPO);

	/**
	 * 修改状态颜色为默认值
	 * 
	 * @param companyId
	 * @param statusId
	 * @param column
	 */
	void editColorToDefault(int companyId, int statusId, String column);
}
