package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.EditClientPhonePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

/**
 * 编辑客资业务层
 */
public interface ClientEditService {

	/**
	 * 电商推广修改客资
	 *
	 * @param clientVO
	 * @param staffPO
	 */
	void editClientByDscj(ClientVO clientVO, StaffPO staffPO);

	/**
	 * 电商邀约修改客资
	 *
	 * @param clientVO
	 * @param staffPO
	 */
	void editClientByDsyy(ClientVO clientVO, StaffPO staffPO);

	/**
	 * 门市修改客资
	 *
	 * @param clientVO
	 * @param staffPO
	 */
	void editClientByMsjd(ClientVO clientVO, StaffPO staffPO);

	/**
	 * 主管纠错
	 *
	 * @param clientVO
	 * @param staffPO
	 */
	void editClientByCwzx(ClientVO clientVO, StaffPO staffPO);

	/**
	 * 修改联系方式日志
	 * @param queryMapDTO
	 * @param clientLogDTO
	 * @return
	 */
	PageInfo editClientPhoneLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

	/**
	 * 微信扫码日志
	 * @param queryMapDTO
	 * @param clientLogDTO
	 * @return
	 */
	PageInfo wechatClientPhoneLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

	/**
	 * 重复客资记录
	 * @param queryMapDTO
	 * @param clientLogDTO
	 * @return
	 */
	PageInfo repateKzLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);


}
