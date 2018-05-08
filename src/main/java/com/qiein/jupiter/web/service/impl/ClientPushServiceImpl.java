package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.GroupKzNumTodayDao;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.GroupKzNumToday;
import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
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

	@Autowired
	private GroupKzNumTodayDao groupKzNumTodayDao;

	@Autowired
	private StaffDao staffDao;

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
		StaffPushDTO appointer = null;

		// 客资分配
		switch (rule) {
		case ChannelConstant.PUSH_RULE_GROUP_STAFF_AVG_ALLOT:
			// 1：小组+员工-指定承接小组依据权重比自动分配
			appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime, interval);
			System.out.println("应分配客服：" + appointer.getStaffId());
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
	public StaffPushDTO getStaffGroupStaffAvg(int companyId, String kzId, int shopId, int channelId, int channelTypeId,
			int overTime, int interval) {

		// 获取客资信息
		ClientPushDTO clientDTO = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId));

		// 判断客资当前状态-限定客资最后推送时间已经超过分配间隔
		if (clientDTO == null || (clientDTO.getPushInterval() != 0 && clientDTO.getPushInterval() < overTime)) {
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

		// 统计当天该拍摄地和渠道，每个小组的客资分配情况
		List<GroupKzNumToday> groupKzNumTodayList = groupKzNumTodayDao.getGroupKzNumTodayByShopChannelId(shopId,
				channelId, companyId, DBSplitUtil.getInfoTabName(companyId));

		// 要分配的目标客服
		StaffPushDTO appointor = null;

		// 值匹配，差比分析
		double maxDiffPid = doGroupDiffCalc(shopChannelGroupRelaList, groupKzNumTodayList);

		while (CollectionUtils.isNotEmpty(shopChannelGroupRelaList)) {

			if (maxDiffPid == -1) {
				maxDiffPid = doGroupDiffCalc(shopChannelGroupRelaList);
			}

			// 取出差比分析后差比值最大的小组即为要分配的客服组
			ShopChannelGroupPO thisGroup = getCurrentGroup(shopChannelGroupRelaList, maxDiffPid);

			// 从当前组找出要可以领取该渠道和拍摄地客资的客服
			appointor = getAppointorByAllotRule(thisGroup.getGroupId(), companyId, kzId, shopId, channelId,
					channelTypeId, interval);

			// 找不到分配客服就移除改组的分配拣选，重新选组
			if (appointor == null) {
				shopChannelGroupRelaList.remove(thisGroup);
				// 差比重置
				maxDiffPid = -1;
			} else {
				return appointor;
			}
		}

		return appointor;
	}

	/**
	 * 从当前组找出要可以领取该渠道和拍摄地客资的客服
	 * 
	 * @param groupId
	 * @param companyId
	 * @param kzId
	 * @param shopId
	 * @param channelId
	 * @param channelTypeId
	 * @param overTime
	 * @param interval
	 * @return
	 */
	private StaffPushDTO getAppointorByAllotRule(String groupId, int companyId, String kzId, int shopId, int channelId,
			int channelTypeId, int interval) {

		// 获取可以领取该渠道和拍摄地的在线的客服集合
		List<StaffPushDTO> staffOnlineList = staffDao.listStaffPushDTOByShopIdAndChannelId(companyId, groupId,
				channelId, shopId, interval);

		if (CollectionUtils.isEmpty(staffOnlineList)) {
			return null;
		}

		int calcRange = 60;

		// 获取从当前时间往前退一个小时内所有客服对该渠道和拍摄地的客资的领取情况
		List<StaffPushDTO> staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId),
				companyId, channelId, shopId, calcRange, staffOnlineList);

		while (calcRange < 540 && (staffAllotList == null || staffAllotList.size() != staffOnlineList.size())) {
			calcRange += 120;
			staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId), companyId,
					channelId, shopId, calcRange, staffOnlineList);
		}

		// 值匹配，差比分析
		double maxDiffPid = doAppointDiffCalc(staffOnlineList, staffAllotList);

		// 取出差比分析后差比值最大的小组即为要分配的客服组
		StaffPushDTO thisAppointor = getCurrentAppointor(staffOnlineList, maxDiffPid);

		// 客服拣选
		return thisAppointor;
	}

	/**
	 * 取出差比分析后差比值最大的小组即为要分配的客服组
	 * 
	 * @param shopChannelGroupRelaList
	 * @param maxDiffPid
	 * @return
	 */
	private ShopChannelGroupPO getCurrentGroup(List<ShopChannelGroupPO> shopChannelGroupRelaList, double maxDiffPid) {

		for (ShopChannelGroupPO thisGroup : shopChannelGroupRelaList) {
			if (thisGroup.getDiffPid() == maxDiffPid) {
				return thisGroup;
			}
		}

		return null;
	}

	/**
	 * 取出差比分析后差比值最大的客服即为要分配的客服
	 * 
	 * @param shopChannelGroupRelaList
	 * @param maxDiffPid
	 * @return
	 */
	private StaffPushDTO getCurrentAppointor(List<StaffPushDTO> staffOnlineList, double maxDiffPid) {

		for (StaffPushDTO thisAppointor : staffOnlineList) {
			if (thisAppointor.getDiffPid() == maxDiffPid) {
				return thisAppointor;
			}
		}

		return null;
	}

	/**
	 * 客服值匹配，差比分析
	 * 
	 * @param staffOnlineList
	 * @param staffAllotList
	 * @return
	 */
	private double doAppointDiffCalc(List<StaffPushDTO> staffOnlineList, List<StaffPushDTO> staffAllotList) {

		if (CollectionUtils.isEmpty(staffAllotList)) {
			return Double.MAX_VALUE;
		}

		double maxDiffPid = 0.0;

		// 值匹配
		for (StaffPushDTO appointor : staffOnlineList) {
			for (StaffPushDTO todayNum : staffAllotList) {
				if (appointor.getStaffId() == todayNum.getStaffId()) {
					appointor.setTodayNum(todayNum.getTodayNum());
					appointor.doCalculateAllotNumDiffPID();
					continue;
				}
			}
			if (appointor.getDiffPid() > maxDiffPid) {
				maxDiffPid = appointor.getDiffPid();
			}
		}

		return maxDiffPid;
	}

	/**
	 * 客服组值匹配，差比分析
	 * 
	 * @param shopChannelGroupRelaList
	 * @param groupKzNumTodayList
	 */
	private double doGroupDiffCalc(List<ShopChannelGroupPO> shopChannelGroupRelaList,
			List<GroupKzNumToday> groupKzNumTodayList) {

		if (CollectionUtils.isEmpty(groupKzNumTodayList)) {
			return Double.MAX_VALUE;
		}

		double maxDiffPid = 0.0;

		// 值匹配
		for (ShopChannelGroupPO rela : shopChannelGroupRelaList) {
			for (GroupKzNumToday todayNum : groupKzNumTodayList) {
				if (rela.getGroupId().equals(todayNum.getGroupId())) {
					rela.setTodayNum(todayNum.getKzNum());
					rela.doCalculateAllotNumDiffPID();
					continue;
				}
			}
			if (rela.getDiffPid() > maxDiffPid) {
				maxDiffPid = rela.getDiffPid();
			}
		}

		return maxDiffPid;
	}

	/**
	 * 取差比最大值
	 * 
	 * @param shopChannelGroupRelaList
	 * @return
	 */
	private double doGroupDiffCalc(List<ShopChannelGroupPO> shopChannelGroupRelaList) {

		double maxDiffPid = 0.0;

		// 值匹配
		for (ShopChannelGroupPO rela : shopChannelGroupRelaList) {
			if (rela.getDiffPid() > maxDiffPid) {
				maxDiffPid = rela.getDiffPid();
			}
		}

		return maxDiffPid;
	}
}