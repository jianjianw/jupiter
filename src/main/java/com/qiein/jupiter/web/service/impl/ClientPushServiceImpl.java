package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.StaffDTO;
import com.qiein.jupiter.web.entity.po.ShopChannelGroupPO;
import com.qiein.jupiter.web.service.ClientPushService;

/**
 * 客资推送
 * 
 * @author JingChenglong 2018/05/08 10:58
 *
 */
@Service
public class ClientPushServiceImpl implements ClientPushService {

	@Autowired
	private ClientInfoDao clientInfoDao;

	@Autowired
	private ShopChannelGroupDao ShopChannelGroupDao;

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

		// 分配目标客服
		StaffDTO appointer = null;

		// 客资分配
		switch (rule) {
		case ChannelConstant.PUSH_RULE_GROUP_STAFF_AVG_ALLOT:
			// 1：小组+员工-指定承接小组依据权重比自动分配
			appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime, interval);
			break;
		default:
			break;
		}

		if (appointer == null) {
			return;
		}
	}

	/**
	 * 小组权重设置分配规则拣选客服
	 * 
	 * @param companyId
	 * @param kzId
	 * @param shopId
	 * @param channelId
	 * @param channelTypeId
	 * @param overTime
	 * @param interval
	 * @return
	 */
	public StaffDTO getStaffGroupStaffAvg(int companyId, String kzId, int shopId, int channelId, int channelTypeId,
			int overTime, int interval) {

		// 获取客资信息
		ClientPushDTO clientDTO = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId));

		// 判断客资当前状态-限定客资最后推送时间已经超过分配间隔
		if (clientDTO.getPushInterval() != 0 && clientDTO.getPushInterval() < overTime) {
			return null;
		}
		// 限定客资状态为分配中，可领取，未接入
		if (clientDTO.getStatusId() != ClientStatusConst.BE_ALLOTING
				&& clientDTO.getStatusId() != ClientStatusConst.BE_WAIT_MAKE_ORDER) {
			return null;
		}

		// 根据拍摄地ID，渠道ID获取要分配的小组ID集合
		List<ShopChannelGroupPO> shopChannelGroupRelaList = ShopChannelGroupDao.listShopChannelGroupRela(companyId,
				shopId, channelId);

		if (CollectionUtils.isEmpty(shopChannelGroupRelaList)) {
			return null;
		}

		// 统计当天改拍摄地和渠道，每个小组的客资分配情况

		// 值匹配，差比分析

		// 拣选客服组
		return null;
	}
}