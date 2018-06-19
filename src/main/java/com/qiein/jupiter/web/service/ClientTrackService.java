package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 客资追踪业务层 gaoxiaoli
 */

public interface ClientTrackService {

	/**
	 * 批量删除客资
	 *
	 * @param kzIds
	 * @param staffPO
	 */
	void batchDeleteKzList(String kzIds, StaffPO staffPO);

	/**
	 * 批量转移客资
	 *
	 * @param kzIds
	 * @param role
	 * @param toStaffId
	 * @param staffPO
	 */
	void batchTransferKzList(String kzIds, String role, int toStaffId, StaffPO staffPO);

	/**
	 * 无效审批
	 *
	 * @param kzIds
	 * @param memo
	 * @param rst
	 * @param invalidLabel
	 * @param staffPO
	 */
	String approvalInvalidKzList(String kzIds, String memo, int rst, String invalidLabel, StaffPO staffPO);

	/**
	 * 客资批量分配
	 *
	 * @param kzIds
	 * @param staffIds
	 * @param companyId
	 */
	void pushLp(String kzIds, String staffIds, int companyId, int operaId, String operaName);
	/**
	 * 批量恢复，回收客资
	 *
	 * @param kzIds
	 * @param staffPO
	 */
	void batchRestoreKzList(String kzIds, StaffPO staffPO);

}
