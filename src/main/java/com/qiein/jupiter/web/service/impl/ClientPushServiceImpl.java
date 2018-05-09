package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientAllotLogDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.GroupKzNumTodayDao;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.GroupKzNumToday;
import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
import com.qiein.jupiter.web.entity.po.AllotLogPO;
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
	private ClientAllotLogDao clientAllotLogDao;
	@Autowired
	private ShopChannelGroupDao ShopChannelGroupDao;
	@Autowired
	private GroupKzNumTodayDao groupKzNumTodayDao;
	@Autowired
	private StaffDao staffDao;

	/**
	 * 旅拍版本客资推送
	 */
	@Transactional
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
		case ChannelConstant.PUSH_RULE_AVG_ALLOT:
			// 1：小组+员工-指定承接小组依据权重比自动分配 - <无需领取>
			appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime, interval);
			if (appointer == null) {
				return;
			}
			// 生成分配日志
			AllotLogPO allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(),
					appointer.getGroupId(), appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);

			// 客资分配客服
			doPushAvgAllot(companyId, kzId, appointer, allotLog.getId(), overTime);
			break;
		case ChannelConstant.PUSH_RULE_AVG_RECEIVE:
			// 11：小组+员工-指定承接小组依据权重比自动分配 - <客户端领取>
			break;
		default:
			break;
		}
	}

	/**
	 * 客资依据权重设置平均分配
	 * 
	 * @param companyId
	 * @param kzId
	 * @param appointer
	 * @param allotLogId
	 * @param overTime
	 */
	private void doPushAvgAllot(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

		// 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，客资最后推送信息
		int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
				kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
				appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
		if (1 != updateRstNum) {
			System.out.println("修改错误");
		}

		updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
				kzId, appointer.getStaffName(), appointer.getGroupName());
		if (1 != updateRstNum) {
			System.out.println("修改错误");
		}

		// 修改指定分配日志状态为已领取
		updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
				allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
		if (1 != updateRstNum) {
			System.out.println("修改错误");
		}

		// 修改客资的最后操作时间，领取时间
		updateRstNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
		if (1 != updateRstNum) {
			System.out.println("修改错误");
		}

		// 修改客服最后推送时间
		updateRstNum = staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
		if (1 != updateRstNum) {
			System.out.println("修改错误");
		}

		// 客资日志记录

		// 推送消息
	}

	/**
	 * 生成客资分配日志
	 * 
	 * @param kzId
	 * @param staffId
	 * @param staffName
	 * @param groupId
	 * @param groupName
	 * @param statusId
	 * @param allotType
	 * @param companyId
	 * @return
	 */
	private AllotLogPO addAllotLog(String kzId, int staffId, String staffName, String groupId, String groupName,
			int allotType, int companyId) {

		// 生成分配日志
		AllotLogPO allotLog = new AllotLogPO(kzId, staffId, staffName, groupId, groupName, allotType, companyId);

		// 记录分配日志
		clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

		if (NumUtil.isInValid(allotLog.getId())) {
			System.out.println("日志记录错误");
		}

		return allotLog;
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
				appointor.setGroupId(thisGroup.getGroupId());
				appointor.setGroupName(thisGroup.getGroupName());
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

		int calcRange = CommonConstant.ALLOT_RANGE_DEFAULT;

		// 获取从当前时间往前退一个小时内所有客服对该渠道和拍摄地的客资的领取情况
		List<StaffPushDTO> staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId),
				companyId, channelId, shopId, calcRange, staffOnlineList);

		while (calcRange <= CommonConstant.ALLOT_RANGE_MAX
				&& (staffAllotList == null || staffAllotList.size() != staffOnlineList.size())) {
			calcRange += CommonConstant.ALLOT_RANGE_INTERVAL;
			staffAllotList = staffDao.listStaffPushDTOByAlloted(DBSplitUtil.getInfoTabName(companyId), companyId,
					channelId, shopId, calcRange, staffOnlineList);
		}

		// 值匹配，差比分析
		double maxDiffPid = doAppointDiffCalc(staffOnlineList, staffAllotList);

		// 取出差比分析后差比值最大的小组即为要分配的客服组
		return getCurrentAppointor(staffOnlineList, maxDiffPid);
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