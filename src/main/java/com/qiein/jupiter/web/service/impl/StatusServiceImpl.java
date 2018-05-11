package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.StatusDao;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 状态管理
 * 
 * @author JingChenglong 2018/05/11 11:50
 *
 */
@Service
public class StatusServiceImpl implements StatusService {

	@Autowired
	private StatusDao statusDao;

	/**
	 * 获取企业状态列表
	 *
	 * @param companyId
	 * @return
	 */
	public List<StatusPO> getCompanyStatusList(int companyId) {
		return statusDao.getCompanyStatusList(companyId);
	}

	/**
	 * 编辑状态
	 *
	 * @param statusPO
	 * @return
	 */
	public void editStatus(StatusPO statusPO) {
		statusDao.editStatus(statusPO);
	}

	/**
	 * 修改状态颜色为默认值
	 */
	@Override
	public void editColorToDefault(int companyId, int id, String column) {

		StatusPO statusColor = statusDao.getStatusById(companyId, id);

		if (statusColor == null) {
			throw new RException(ExceptionEnum.STS_GET_ERROR);
		}

		StatusPO defaultColor = statusDao.getStatusByStatusId(CommonConstant.DEFAULT_COMPID, statusColor.getStatusId());

		if (defaultColor == null) {
			throw new RException(ExceptionEnum.STS_DEFAULT_ERROR);
		}

		statusDao.editStatus(
				new StatusPO(id, StatusPO.STS_BGCOLOR.equals(column) ? defaultColor.getBackColor() : "",
						StatusPO.STS_FONTCOLOR.equals(column) ? defaultColor.getFontColor() : "", companyId));
	}
}