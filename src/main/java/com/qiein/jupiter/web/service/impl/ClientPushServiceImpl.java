package com.qiein.jupiter.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.msg.goeasy.MessageConts;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientAllotLogDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.dao.ClientTimerDao;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.dao.GroupKzNumTodayDao;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.dao.ShopChannelGroupDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.dao.StaffStatusLogDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.GroupKzNumToday;
import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
import com.qiein.jupiter.web.entity.po.AllotLogPO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.po.ClientTimerPO;
import com.qiein.jupiter.web.entity.po.NewsPO;
import com.qiein.jupiter.web.entity.po.ShopChannelGroupPO;
import com.qiein.jupiter.web.entity.po.StaffStatusLog;
import com.qiein.jupiter.web.service.ClientPushService;

/**
 * 客资推送
 *
 * @author JingChenglong 2018/05/08 10:58
 */
@Service
public class ClientPushServiceImpl implements ClientPushService {

	@Autowired
	private ClientInfoDao clientInfoDao;
	@Autowired
	private ClientLogDao clientLogDao;
	@Autowired
	private ClientAllotLogDao clientAllotLogDao;
	@Autowired
	private ShopChannelGroupDao shopChannelGroupDao;
	@Autowired
	private GroupKzNumTodayDao groupKzNumTodayDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private StaffStatusLogDao statusLogDao;
	@Autowired
	private NewsDao newsDao;
	@Autowired
	private ClientTimerDao clientTimerDao;
	@Autowired
	private CompanyDao companyDao;

	/**
	 * 根据拍摄地和渠道维度推送客资
	 */
	@Transactional
	@Override
	public synchronized void pushLp(int rule, int companyId, String kzId, int shopId, int channelId, int channelTypeId,
			int overTime, int interval) {

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
		AllotLogPO allotLog = null;

		// 获取客资信息
		ClientPushDTO clientDTO = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
				DBSplitUtil.getDetailTabName(companyId));

		// 判断客资当前状态-限定客资最后推送时间已经超过分配间隔
		if (clientDTO == null || (clientDTO.getPushInterval() != 0 && clientDTO.getPushInterval() < overTime)) {
			return;
		}
		// 判定是否已经预选客服
		if (NumUtil.isValid(clientDTO.getAppointorId())
				&& clientDTO.getStatusId() == ClientStatusConst.BE_HAVE_MAKE_ORDER) {
			ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(clientDTO.getKzId(),
					DBSplitUtil.getInfoTabName(companyId), DBSplitUtil.getDetailTabName(companyId));
			GoEasyUtil.pushInfoComed(companyId, clientDTO.getAppointorId(), infoDTO, newsDao);
			GoEasyUtil.pushInfoRefresh(companyId, clientDTO.getAppointorId());
			return;
		}
		// 限定客资状态为分配中，可领取，未接入
		if (clientDTO.getStatusId() != ClientStatusConst.BE_ALLOTING
				&& clientDTO.getStatusId() != ClientStatusConst.BE_WAIT_MAKE_ORDER) {
			return;
		}

		switch (rule) {
		case ChannelConstant.PUSH_RULE_AVG_ALLOT:
			// 1：小组+员工-指定承接小组依据权重比自动分配 - <无需领取>
			appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime, interval);
			if (appointer == null) {
				return;
			}
			// 生成分配日志
			allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
					appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);

			// 客资分配客服
			doPushAvgAllot(companyId, kzId, appointer, allotLog.getId(), overTime);
			break;
		case ChannelConstant.PUSH_RULE_AVG_RECEIVE:
			// 11：小组+员工-指定承接小组依据权重比自动分配 - <客户端领取>
			// 校验之前的客服是否连续怠工
			if (NumUtil.isValid(clientDTO.getAppointorId())) {
				checkOffLine(clientDTO.getAppointorId(), companyId, overTime);
			}
			appointer = getStaffGroupStaffAvg(companyId, kzId, shopId, channelId, channelTypeId, overTime, interval);
			if (appointer == null) {
				return;
			}
			// 生成分配日志
			allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
					appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);

			// 客资分配客服
			doPushAvgReceive(companyId, kzId, appointer, allotLog.getId(), overTime);
			break;
		case ChannelConstant.PUSH_RULE_SELF:
			// 5：回馈个人-谁录分给谁
			if (NumUtil.isValid(clientDTO.getCollectorId())) {
				appointer = staffDao.getPushDTOByCidAndUid(clientDTO.getCollectorId(), companyId);
				// 生成分配日志
				allotLog = addAllotLog(kzId, appointer.getStaffId(), appointer.getStaffName(), appointer.getGroupId(),
						appointer.getGroupName(), ClientConst.ALLOT_SYSTEM_AUTO, companyId);

				// 客资分配客服
				doBlindSelfReceive(companyId, kzId, appointer, allotLog.getId(), overTime);
				break;
			}
		default:
			break;
		}
	}

	/**
	 * 连续怠工三次自动下线
	 *
	 * @param appointId
	 * @param companyId
	 * @param overTime
	 */
	private void checkOffLine(int appointId, int companyId, int overTime) {
		int checkNum = staffDao.getSaboteurCheckNum(DBSplitUtil.getAllotLogTabName(companyId), companyId, appointId,
				overTime);
		if (0 == checkNum) {
			// 连续三次怠工，强制下线
			int i = staffDao.editStatusFlagOffLine(companyId, appointId, StaffStatusEnum.OffLine.getStatusId());
			if (1 == i) {
				// 记录下线日志
				statusLogDao.insert(new StaffStatusLog(appointId, StaffStatusEnum.OffLine.getStatusId(),
						CommonConstant.SYSTEM_OPERA_ID, CommonConstant.SYSTEM_OPERA_NAME, companyId,
						ClientLogConst.CONTINUOUS_SABOTEUR_DONW));
				// 推送状态重载消息
				GoEasyUtil.pushStatusRefresh(companyId, appointId);
				// 推送连续三次怠工下线消息
				GoEasyUtil.pushOffLineAuto(companyId, appointId, newsDao);
			}
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

		// 修改客资信息
		updateInfoWhenReceive(companyId, kzId, allotLogId, appointer);

		// 重载客服今日领取客资数
		resizeTodayNum(companyId, appointer.getStaffId());

		// 推送消息
		ClientGoEasyDTO infoDTO = clientInfoDao.getClientGoEasyDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
				DBSplitUtil.getDetailTabName(companyId));
		GoEasyUtil.pushInfoComed(companyId, appointer.getStaffId(), infoDTO, newsDao);
		GoEasyUtil.pushInfoRefresh(companyId, appointer.getStaffId());
	}

	/**
	 * 客资领取时修改客资信息
	 *
	 * @param companyId
	 * @param kzId
	 * @param allotLogId
	 * @param appointer
	 */
	private void updateInfoWhenReceive(int companyId, String kzId, int allotLogId, StaffPushDTO appointer) {
		// 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
		int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
				kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
				appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
		}

		updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
				kzId, appointer.getStaffName(), appointer.getGroupName());
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
		}

		// 修改指定分配日志状态为已领取
		updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
				allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
		}

		// 修改客资的领取时间
		updateRstNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
		}

		// 修改客服最后推送时间
		updateRstNum = staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
		}

		// 客资日志记录
		updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
				new ClientLogPO(kzId,
						ClientLogConst.getAutoAllotLog(appointer.getGroupName(), appointer.getStaffName()),
						ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.LOG_ERROR);
		}
	}

	/**
	 * 客资依据权重设置平均分配--领取
	 *
	 * @param companyId
	 * @param kzId
	 * @param appointer
	 * @param allotLogId
	 * @param overTime
	 */
	private void doPushAvgReceive(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

		// 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
		int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
				kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_ALLOTING, appointer.getStaffId(),
				appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
		}

		updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
				kzId, appointer.getStaffName(), appointer.getGroupName());
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
		}

		// 修改客服最后推送时间
		updateRstNum = staffDao.updateStaffLastPushTime(companyId, appointer.getStaffId());
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
		}

		// 客资日志记录
		updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
				new ClientLogPO(kzId,
						ClientLogConst.getAutoReceiveLog(appointer.getGroupName(), appointer.getStaffName()),
						ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.LOG_ERROR);
		}

		// 推送消息
		GoEasyUtil.pushAppInfoReceive(companyId, appointer.getStaffId(), 1, kzId, String.valueOf(allotLogId), overTime);
	}

	/**
	 * 客资绑定客服为自己
	 * 
	 * @param companyId
	 * @param kzId
	 * @param appointer
	 * @param allotLogId
	 * @param overTime
	 */
	private void doBlindSelfReceive(int companyId, String kzId, StaffPushDTO appointer, int allotLogId, int overTime) {

		// 客资绑定客服，修改客资状态，客资客服ID，客服名，客资分类，客资客服组信息，最后操作时间，客资最后推送时间
		int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId, DBSplitUtil.getInfoTabName(companyId),
				kzId, ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER, appointer.getStaffId(),
				appointer.getGroupId(), ClientConst.ALLOT_SYSTEM_AUTO);
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
		}

		updateRstNum = clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
				kzId, appointer.getStaffName(), appointer.getGroupName());
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
		}

		// 修改指定分配日志状态为已领取
		updateRstNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
				allotLogId, ClientConst.ALLOT_LOG_STATUS_YES, "now");
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
		}

		// 修改客资的领取时间
		updateRstNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
		}

		// 客资日志记录
		updateRstNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
				new ClientLogPO(kzId,
						ClientLogConst.getAutoAllotLog(appointer.getGroupName(), appointer.getStaffName()),
						ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
		if (1 != updateRstNum) {
			throw new RException(ExceptionEnum.LOG_ERROR);
		}
	}

	/**
	 * 生成客资分配日志
	 *
	 * @param kzId
	 * @param staffId
	 * @param staffName
	 * @param groupId
	 * @param groupName
	 * @param allotType
	 * @param companyId
	 * @return
	 */
	private AllotLogPO addAllotLog(String kzId, int staffId, String staffName, String groupId, String groupName,
			int allotType, int companyId) {

		// 生成分配日志
		AllotLogPO allotLog = new AllotLogPO(kzId, staffId, staffName, groupId, groupName, allotType, companyId,
				CommonConstant.SYSTEM_OPERA_ID, CommonConstant.SYSTEM_OPERA_NAME);

		// 记录分配日志
		clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

		if (NumUtil.isInValid(allotLog.getId())) {
			throw new RException(ExceptionEnum.ALLOT_LOG_ERROR);
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

		// 根据拍摄地ID，渠道ID获取要分配的小组ID集合
		List<ShopChannelGroupPO> shopChannelGroupRelaList = shopChannelGroupDao.listShopChannelGroupRela(companyId,
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

			if (maxDiffPid == Double.MAX_VALUE) {
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
				maxDiffPid = Double.MAX_VALUE;
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
	 * @param staffOnlineList
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

		double maxDiffPid = -Double.MAX_VALUE;

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

		double maxDiffPid = -Double.MAX_VALUE;

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

		double maxDiffPid = -Double.MAX_VALUE;

		// 值匹配
		for (ShopChannelGroupPO rela : shopChannelGroupRelaList) {
			if (rela.getDiffPid() > maxDiffPid) {
				maxDiffPid = rela.getDiffPid();
			}
		}

		return maxDiffPid;
	}

	/**
	 * 计算员工今日客资数，并校验是否满限状态
	 *
	 * @param companyId
	 * @param staffId
	 */
	private void resizeTodayNum(int companyId, int staffId) {
		// 计算客服今日领取客资数
		int num = staffDao.getTodayKzNum(companyId, staffId, DBSplitUtil.getInfoTabName(companyId));
		// 修改今日领取客资数
		int updateNum = staffDao.updateTodatKzNum(companyId, staffId, num);
		if (1 != updateNum) {
			throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
		}
		// 计算是否满限
		updateNum = staffDao.checkOverFlowToday(companyId, staffId);
		if (1 == updateNum) {
			// 推送状态重载消息
			GoEasyUtil.pushStatusRefresh(companyId, staffId);
		}
	}

	/**
	 * 获取需要推送的客资信息
	 *
	 * @param companyId
	 * @return
	 */
	public List<ClientPushDTO> getInfoListBeReadyPush(int companyId, int interval) {
		try {
			return clientInfoDao.getInfoListBeReadyPush(DBSplitUtil.getInfoTabName(companyId), companyId, interval);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 客资批量分配
	 */
	@Override
	public void pushLp(String kzIds, String staffIds, int companyId, int operaId, String operaName) {
		if (StringUtil.isEmpty(kzIds) || StringUtil.isEmpty(staffIds) || NumUtil.isInValid(companyId)) {
			throw new RException(ExceptionEnum.ALLOT_ERROR);
		}
		// 查询所选客资里面没有分出去的客资
		List<ClientPushDTO> infoList = clientInfoDao.listClientsInStrKzids(kzIds, companyId,
				DBSplitUtil.getInfoTabName(companyId));
		if (CollectionUtils.isEmpty(infoList)) {
			throw new RException(ExceptionEnum.ALLOTED_ERROR);
		}
		// 查询所选客服集合
		List<StaffPushDTO> staffList = staffDao.listStaffInstrIds(companyId, staffIds);
		if (staffList == null || staffList.size() == 0) {
			throw new RException(ExceptionEnum.APPOINTOR_ERROR);
		}
		while (infoList.size() != 0) {
			for (StaffPushDTO staff : staffList) {
				if (infoList.size() > 0) {
					// 客资修改最后消息推送时间为当前系统时间，绑定客服，修改状态为分配中
					int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId,
							DBSplitUtil.getInfoTabName(companyId), infoList.get(0).getKzId(),
							ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_ALLOTING, staff.getStaffId(),
							staff.getGroupId(), ClientConst.ALLOT_HANDLER);
					if (1 != updateRstNum) {
						throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
					}

					// 客资修改客资的客服组ID，和客服组名称
					clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
							infoList.get(0).getKzId(), staff.getStaffName(), staff.getGroupName());
					staff.doAddKzIdsWill(infoList.get(0).getKzId());
					infoList.remove(0);
				} else {
					break;
				}
			}
		}

		for (StaffPushDTO staff : staffList) {
			push(companyId, staff.getWillHaveKzidsStrBf(), staff, operaId, operaName);
		}
	}

	public void push(int companyId, String kzIds, StaffPushDTO appoint, int operaId, String operaName) {

		// 根据每个客资生成对应的分配日志
		String[] kzIdsArr = kzIds.split(",");
		String[] allogIdsArr = new String[kzIdsArr.length];
		for (int i = 0; i < kzIdsArr.length; i++) {
			// 生成分配日志
			AllotLogPO allotLog = new AllotLogPO(kzIdsArr[i], appoint.getStaffId(), appoint.getStaffName(),
					appoint.getGroupId(), appoint.getGroupName(), ClientConst.ALLOT_HANDLER, companyId, operaId,
					operaName);

			// 记录分配日志
			clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

			allogIdsArr[i] = String.valueOf(allotLog.getId());
			// 客资日志记录
			clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
					new ClientLogPO(kzIdsArr[i],
							ClientLogConst.getAutoAllotLog(appoint.getGroupName(), appoint.getStaffName()),
							ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
		}

		int overTime = companyDao.getById(companyId).getOvertime();
		if (overTime == 0) {
			overTime = CommonConstant.DEFAULT_OVERTIME;
		}

		// 推送消息
		GoEasyUtil.pushAppInfoReceive(companyId, appoint.getStaffId(), kzIdsArr.length, arrToStr(kzIdsArr),
				arrToStr(allogIdsArr), overTime);
	}

	public static String arrToStr(String[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (StringUtil.isEmpty(arr[i])) {
				continue;
			}
			sb.append(arr[i]);
			sb.append(",");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		return str;
	}

	/**
	 * 定时推送消息，需要追踪的客资
	 */
	@Override
	public void pushClientNoticeInfo() {
		List<ClientTimerPO> allClientTimerList = clientTimerDao.getAll();
		if (CollectionUtils.isNotEmpty(allClientTimerList)) {
			List<Integer> idsDel = new ArrayList<>();
			// 每个公司一个List
			Map<String, List<NewsPO>> companyMap = new HashMap<>();
			for (ClientTimerPO clientTimerPO : allClientTimerList) {
				// 推送消息
				GoEasyUtil.pushWarnTimer(clientTimerPO.getCompanyId(), clientTimerPO.getStaffId(),
						clientTimerPO.getKzId(), clientTimerPO.getMsg());
				idsDel.add(clientTimerPO.getId());
				// 新加一条消息
				NewsPO news = new NewsPO();
				news.setStaffId(clientTimerPO.getStaffId());
				news.setType(MessageConts.MSG_TYPE_NOTICE);
				news.setCompanyId(clientTimerPO.getCompanyId());
				news.setHead(MessageConts.TO_BE_TRACKED_HEAD);
				news.setMsg(clientTimerPO.getMsg());
				news.setKzid(clientTimerPO.getKzId());
				String tableName = DBSplitUtil.getNewsTabName(clientTimerPO.getCompanyId());
				if (companyMap.get(tableName) == null) {
					companyMap.put(tableName, new ArrayList<NewsPO>());
				}
				companyMap.get(tableName).add(news);
			}
			// 添加消息记录
			for (String tableName : companyMap.keySet()) {
				newsDao.batchInsertNews(tableName, companyMap.get(tableName));
			}
			// 删掉已经推送的
			Integer[] idsDelInt = idsDel.toArray(new Integer[idsDel.size()]);
			clientTimerDao.batchDelAready(idsDelInt);
		}
	}

}