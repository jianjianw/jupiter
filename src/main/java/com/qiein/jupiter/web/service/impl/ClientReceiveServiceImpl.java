package com.qiein.jupiter.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientAllotLogDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.service.ClientReceiveService;

/**
 * 客资领取
 * 
 * @author JingChenglong 2018/05/09 17:09
 *
 */
@Service
public class ClientReceiveServiceImpl implements ClientReceiveService {

	@Autowired
	private ClientInfoDao clientInfoDao;
	@Autowired
	private ClientLogDao clientLogDao;
	@Autowired
	private ClientAllotLogDao clientAllotLogDao;

	@Override
	public void receive(String kzId, String logId, int companyId, int staffId, String staffName) {
		if (StringUtil.haveEmpty(kzId, logId) || NumUtil.haveInvalid(companyId, staffId)) {
			throw new RException(ExceptionEnum.INFO_ERROR);
		}
		if (kzId.length() > 32 && kzId.indexOf(CommonConstant.STR_SEPARATOR) != -1) {
			// 多个客资领取
			receive(kzId.split(CommonConstant.STR_SEPARATOR), logId.split(CommonConstant.STR_SEPARATOR), companyId,
					staffId, staffName);
		} else {
			// 一个客资领取
			receive(kzId, Integer.valueOf(logId), companyId, staffId, staffName);
		}
	}

	@Override
	public void refuse(String kzId, String logId, int companyId, int staffId, String staffName) {

	}

	/**
	 * 领取一个客资
	 * 
	 * @param kzId
	 * @param logId
	 * @param companyId
	 * @param staffId
	 */
	private void receive(String kzId, int logId, int companyId, int staffId, String staffName) {
		ClientPushDTO info = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
				DBSplitUtil.getDetailTabName(companyId));
		if (info == null) {
			throw new RException(ExceptionEnum.INFO_ERROR);
		}
		if (staffId != info.getAppointorId()) {
			throw new RException(ExceptionEnum.INFO_OTHER_APPOINTOR);
		}
		if (ClientStatusConst.BE_ALLOTING != info.getStatusId()
				&& ClientStatusConst.BE_WAIT_MAKE_ORDER != info.getStatusId()) {
			throw new RException(ExceptionEnum.INFO_BE_RECEIVED);
		}

		// 修改客资状态为未设置
		int updateNum = clientInfoDao.updateClientInfoStatus(companyId, DBSplitUtil.getInfoTabName(companyId), kzId,
				ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER);
		if (1 != updateNum) {
			System.out.println("更新失败");
		}

		// 添加客资领取日志
		updateNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId), new ClientLogPO(kzId, staffId,
				staffName, ClientLogConst.INFO_LOG_RECEIVE, ClientLogConst.INFO_LOGTYPE_RECEIVE, companyId));
		if (1 != updateNum) {
			System.out.println("更新失败");
		}

		// 修改客资分配日志状态为已领取
		updateNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId, logId,
				ClientConst.ALLOT_LOG_STATUS_YES, "now");
		if (1 != updateNum) {
			System.out.println("修改错误");
		}

		// 计算客服今日领取客资数

		// 推送页面重载客资列表
	}

	/**
	 * 领取多个客资
	 */
	private void receive(String[] kzIdArr, String[] logIdArr, int companyId, int staffId, String staffName) {

	}
}