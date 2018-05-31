package com.qiein.jupiter.web.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.constant.PmsConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.dao.SchedulingDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.dao.StaffStatusLogDao;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffStatusLog;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;
import com.qiein.jupiter.web.service.SchedulingService;

/**
 * Created by Administrator on 2018/5/10 0010.
 */
@Service
public class SchedulingServiceImpl implements SchedulingService {

	@Autowired
	private SchedulingDao schedulingDao;

	@Autowired
	private ShopDao shopDao;

	@Autowired
	private StaffDao staffDao;

	@Autowired
	private ChannelDao channelDao;

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private GroupStaffDao groupStaffDao;

	@Autowired
	private StaffStatusLogDao staffStatusLogDao;

	/**
	 * 根据类型获取部门列表
	 *
	 * @param type
	 * @param companyId
	 * @return
	 */
	@Override
	public List<GroupsInfoVO> getDeptListByType(String type, int companyId, int staffId) {
		// TODO 加入缓存
		// 获取了全部符合类型的部门
		List<GroupsInfoVO> list = schedulingDao.getDeptListByType(type.startsWith("ds") ? "dsyy" : "zjsyy", companyId);
		// 根据权限过滤显示标记
		// 获取员工权限
		List<Integer> roleList = rolePermissionDao.getStaffPmsList(companyId, staffId);
		// 获取员工所在部门列表
		List<String> deptList = groupDao.getDeptByTypeAndStaff(companyId, staffId, type);

		if (roleList.contains(PmsConstant.SEE_ALL)) { // 查看所有 所有的都显示
			for (GroupsInfoVO giv : list) {
				giv.setShowFlag(true);
			}
		} else if (roleList.contains(PmsConstant.SEE_MY_DEPT) || roleList.contains(PmsConstant.SEE_MYSELF)
				|| roleList.contains(PmsConstant.SEE_MY_GROUP)) { // 查看部门
			for (GroupsInfoVO giv : list) {
				if (deptList.contains(giv.getGroupId()) || (!StringUtil.isEmpty(giv.getChiefIds())
						&& Arrays.asList(giv.getChiefIds().split(",")).contains(String.valueOf(staffId)))) {
					giv.setShowFlag(true);
				}
			}
		}
		/*
		 * else if (roleList.contains(89)||roleList.contains(90)){ // 查看小组 for
		 * (GroupsInfoVO giv:list){ if (deptList.contains(giv.getGroupId())||
		 * Arrays.asList(giv.getChiefIds().split(",")).contains(String.valueOf(
		 * staffId))){ giv.setShowFlag(true); } } }
		 */
		return list;
	}

	/**
	 * 过滤部门列表,基于在线人数和接单数
	 *
	 * @param list
	 * @param companyId
	 * @param staffId
	 * @return
	 */
	@Override
	public List<GroupsInfoVO> filterDeptList(List<GroupsInfoVO> list, int companyId, int staffId) {
		List<GroupsInfoVO> qxList = schedulingDao.getDeptLineNumAndOrderNum(companyId);
		doFilterGroupList(list, qxList);
		return list;
	}

	/**
	 * 根据部门编号获取部门下所有小组
	 *
	 * @param deptId
	 * @param companyId
	 * @return
	 */
	@Override
	public List<GroupsInfoVO> getGroupListByDept(String deptId, int companyId, int staffId) {
		// TODO 加入缓存
		// 获取了全部符合类型的部门
		List<GroupsInfoVO> list = schedulingDao.getGroupListByDept(deptId, companyId);
		// 根据权限过滤显示标记
		// 获取员工权限
		List<Integer> roleList = rolePermissionDao.getStaffPmsList(companyId, staffId);
		// 获取员工所在小组或主管小组列表
		List<String> groupList = groupDao.getGroupByStaffAndType(companyId, staffId, null);
		// 获取各小组内人员的接单数和在线人数
		List<GroupsInfoVO> infoList = groupStaffDao.getStaffMarsInfo(companyId);

		if (roleList.contains(PmsConstant.SEE_ALL) || roleList.contains(PmsConstant.SEE_MY_DEPT)) { // 查看所有
																									// 所有的都显示
			forShowFlag(list, true);
		} else if (roleList.contains(PmsConstant.SEE_MYSELF) || roleList.contains(PmsConstant.SEE_MY_GROUP)) { // 查看小组
			for (GroupsInfoVO giv : list) {
				if (groupList.contains(giv.getGroupId()) || (!StringUtil.isEmpty(giv.getChiefIds())
						&& Arrays.asList(giv.getChiefIds().split(",")).contains(String.valueOf(staffId)))) {
					giv.setShowFlag(true);
				}
			}
		} else {
			list = null;
		}
		return list;
	}

	/**
	 * 过滤小组列表,基于在线人数和接单数
	 *
	 * @param list
	 * @param companyId
	 * @return
	 */
	@Override
	public List<GroupsInfoVO> filterGroupList(List<GroupsInfoVO> list, String deptId, int companyId) {

		List<GroupsInfoVO> qxList = schedulingDao.getGroupLineNumAndOrderNum(companyId, deptId);
		doFilterGroupList(list, qxList);
		return list;
	}

	private void doFilterGroupList(List<GroupsInfoVO> list, List<GroupsInfoVO> qxList) {
		for (GroupsInfoVO siv : list) {
			for (GroupsInfoVO qxsiv : qxList) {
				if (qxsiv.getGroupId().equals(siv.getGroupId())) {
					siv.setLineNum(qxsiv.getLineNum());
					siv.setOrderNum(qxsiv.getOrderNum());
				}
			}
		}
	}

	/**
	 * 给查看全部修改显示标记
	 *
	 * @param list
	 * @param flag
	 */
	private static void forShowFlag(List<GroupsInfoVO> list, boolean flag) {
		for (GroupsInfoVO giv : list) {
			giv.setShowFlag(true);
		}
	}

	/**
	 * 编辑网销排班员工
	 *
	 * @param staffMarsDTO
	 */
	@Override
	public void editStaffMars(StaffMarsDTO staffMarsDTO) {

		// id不能为空
		if (staffMarsDTO.getId() == 0) {
			throw new RException(ExceptionEnum.ID_IS_NULL);
		}

		if (staffMarsDTO.getLimitShopIds() != null && staffMarsDTO.getLimitShopIds().trim().length() != 0) { // 如果限制拍摄地，改不接单的拍摄地则同时修改拍摄地名称
			List<String> list = shopDao.getLimitShopNamesByIds(staffMarsDTO.getLimitShopIds().split(","),
					staffMarsDTO.getCompanyId());
			StringBuilder limitShopNames = new StringBuilder();
			for (String s : list)
				limitShopNames.append(s).append(",");
			staffMarsDTO.setLimitShopNames(limitShopNames.substring(0, limitShopNames.length() - 1));
		} else if (staffMarsDTO.getLimitShopIds() != null && staffMarsDTO.getLimitShopIds().trim().length() == 0) { // 如果不限制拍摄地，将拍摄地名称改为空字符串
			staffMarsDTO.setLimitShopNames("");
		}

		if (staffMarsDTO.getLimitChannelIds() != null && staffMarsDTO.getLimitChannelIds().trim().length() != 0) { // 如果限制来源，修改不接单的渠道同时修改渠道名称
			List<String> list = channelDao.getChannelNamesByIds(staffMarsDTO.getCompanyId(),
					staffMarsDTO.getLimitChannelIds().split(","));
			StringBuilder limitChannelNames = new StringBuilder();
			for (String s : list)
				limitChannelNames.append(s).append(",");
			staffMarsDTO.setLimitChannelNames(limitChannelNames.substring(0, limitChannelNames.length() - 1));
		} else if (staffMarsDTO.getLimitChannelIds() != null
				&& staffMarsDTO.getLimitChannelIds().trim().length() == 0) {
			staffMarsDTO.setLimitChannelNames("");
		}

		// 员工详情
		StaffDetailVO staff = staffDao.getStaffDetailVO(staffMarsDTO.getId(), staffMarsDTO.getCompanyId());
		if (staffMarsDTO.getLimitDay() != null) { // 改了日接单限额
			if (staff.getTodayNum() >= staffMarsDTO.getLimitDay()) { // 如果今日接单数大于等于接单上限，设置为满限状态
				staffMarsDTO.setStatusFlag(9);
			} else {
				if (staff.getStatusFlag() == 9) { // 如果之前是满限状态，更改为已停单
					staffMarsDTO.setStatusFlag(0);
					// TODO 添加上下线日志 修改上下线时间
					staffStatusLogDao.insert(new StaffStatusLog(staffMarsDTO.getId(), staffMarsDTO.getStatusFlag(), staffMarsDTO.getOperaId(),
							staffMarsDTO.getOperaName(), staffMarsDTO.getCompanyId(),
							staffMarsDTO.getOperaName() + " 将 " + staff.getNickName() + " 状态修改为下线"));
					StaffDetailPO staffDetailPO = new StaffDetailPO();
					staffDetailPO.setLastLogoutIp("");
					staffDao.updateStaffLogoutInfo(staffDetailPO);
				}
			}
		}

		if (staffMarsDTO.getStatusFlag() != null) {
			if (staffMarsDTO.getStatusFlag() == 0 || staffMarsDTO.getStatusFlag() == 1) {
				StaffDetailPO staffDetailPO = new StaffDetailPO();
				staffDetailPO.setCompanyId(staffMarsDTO.getCompanyId());
				staffDetailPO.setId(staffMarsDTO.getId());
				// TODO 添加上下线日志 修改上下线时间
				staffStatusLogDao.insert(new StaffStatusLog(staffMarsDTO.getId(), staffMarsDTO.getStatusFlag(), staffMarsDTO.getOperaId(),
						staffMarsDTO.getOperaName(), staffMarsDTO.getCompanyId(),
						staffMarsDTO.getOperaName() + " 将 " + staff.getNickName() + " 的状态修改为"
								+ (staffMarsDTO.getStatusFlag() == 0 ? "下线" : "上线")));
				if (staffMarsDTO.getStatusFlag() == 0) {
					staffDetailPO.setLastLogoutIp("");
					staffDao.updateStaffLogoutInfo(staffDetailPO);
				} else {
					staffDetailPO.setLastLoginIp("");
					staffDao.updateStaffLoginInfo(staffDetailPO);
				}
			}
		}

		if (schedulingDao.update(staffMarsDTO) == 0) {
			throw new RException(ExceptionEnum.EDIT_FAIL);
		}

		// 推送状态重载消息
		GoEasyUtil.pushStatusRefresh(staffMarsDTO.getCompanyId(), staffMarsDTO.getId());
	}
}
