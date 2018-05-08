package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.StaffDTO;
import com.qiein.jupiter.web.service.ClientPushService;
import org.springframework.stereotype.Service;

/**
 * 客资推送
 * 
 * @author JingChenglong 2018/05/08 10:58
 *
 */
@Service
public class ClientPushServiceImpl implements ClientPushService {

	@Override
	public void pushLp(int rule, int companyId, String kzId, int shopId, int channelId, int channelTypeId, int overTime,
			int interval) {

		if (NumUtil.haveInvalid(rule, companyId, shopId, channelId, channelTypeId) || StringUtil.isEmpty(kzId)) {
			return;
		}

		if (overTime == 0) {
			overTime = CommonConstant.DEFAULT_OVERTIME;
		}
		if (interval == 0) {
			interval = CommonConstant.DEFAULT_INTERVAL;
		}

		// 客资分配
		switch (rule) {
		case ChannelConstant.PUSH_RULE_GROUP_STAFF_AVG_ALLOT:
			// 1：小组+员工-指定承接小组依据权重比自动分配
			StaffDTO appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime,
					interval);
			break;
		default:
			break;
		}
	}

	public StaffDTO getStaffGroupStaffAvg(int companyId, String kzId, int shopId, int channelId, int channelTypeId,
			int overTime, int interval) {

		// 获取客资信息
		ClientPushDTO clientDTO = new ClientPushDTO();

		return null;
	}
}