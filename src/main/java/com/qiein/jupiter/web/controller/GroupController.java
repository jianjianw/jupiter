package com.qiein.jupiter.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.SysLogUtil;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import com.qiein.jupiter.web.service.GroupService;
import com.qiein.jupiter.web.service.ShopChannelGroupService;
import com.qiein.jupiter.web.service.SystemLogService;

/**
 * group api
 */
@RestController
@RequestMapping("/group")
@Validated
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	@Autowired
	private SystemLogService logService;
	@Autowired
	private ShopChannelGroupService ShopChannelGroupService;

	@GetMapping("/get_company_all_dept_list")
	public ResultInfo getCompanyAllDeptList() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		List<GroupVO> list = groupService.getCompanyAllDeptList(currentLoginStaff.getCompanyId());
		return ResultInfoUtil.success(list);
	}

	/**
	 * 更新
	 *
	 * @return
	 */
	@PostMapping("/update")
	public ResultInfo update(@Validated @RequestBody GroupPO groupPO) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		groupPO.setCompanyId(currentLoginStaff.getCompanyId());
		// 参数去trim
		ObjectUtil.objectStrParamTrim(groupPO);
		groupService.update(groupPO);
		// 同步修改分配表中的客服组名
		ShopChannelGroupService.updateGroupNameById(groupPO.getGroupName(), groupPO.getGroupId(),
				groupPO.getCompanyId());
		return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
	}

	/**
	 * 新增
	 *
	 * @return
	 */
	@PostMapping("/insert")
	public ResultInfo insert(@Validated @RequestBody GroupPO groupPO) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		groupPO.setCompanyId(currentLoginStaff.getCompanyId());
		// 参数去trim
		ObjectUtil.objectStrParamTrim(groupPO);
		groupService.insert(groupPO);
		// 日志记录
		logService
				.addLog(new SystemLog(SysLogUtil.LOG_TYPE_GROUP, currentLoginStaff.getIp(), currentLoginStaff.getUrl(),
						currentLoginStaff.getId(),
						currentLoginStaff.getUserName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_GROUP,
								groupPO.getGroupName(), groupPO.getGroupType(), groupPO.getChiefNames()),
						currentLoginStaff.getCompanyId()));
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@GetMapping("/delete")
	public ResultInfo delete(@Id Integer id) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		groupService.delete(id, currentLoginStaff.getCompanyId());
		return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
	}

	/**
	 * 获取当前公司所有的下属部门及员工
	 */
	@GetMapping("/get_all_dept_and_staff")
	public ResultInfo getAllDeptAndStaff() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil.success(groupService.getAllDeptAndStaff(currentLoginStaff.getCompanyId()));
	}

	/**
	 * 根据角色，获取小组及人员下拉
	 */
	@GetMapping("/get_group_staff_by_role")
	public ResultInfo getGroupStaffByRole(@NotEmptyStr @RequestParam("role") String role) {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil.success(
				groupService.getGroupStaffByType(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(), role));
	}

	/**
	 * 获取邀约客服小组及人员
	 */
	@GetMapping("/get_dsyy_group_staff_list")
	public ResultInfo getDsyyGroupStaffList() {
		// 获取当前登录账户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		return ResultInfoUtil.success(groupService.getDsyyGroupStaffList(currentLoginStaff.getCompanyId()));
	}

}
