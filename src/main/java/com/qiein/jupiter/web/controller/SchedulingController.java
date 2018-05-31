package com.qiein.jupiter.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.SchedulingService;
import com.qiein.jupiter.web.service.StaffService;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;
import com.qiein.jupiter.web.service.GroupService;

/**
 * 网销排班 Controller
 * Created by Tt on 2018/5/8 0008.
 */
@RestController
@RequestMapping("/staffmars")
public class SchedulingController extends BaseController {

	@Resource
	private GroupService groupService;

	@Resource
	private StaffService staffService;

	@Resource
	private SchedulingService schedulingService;

	/**
	 * 根据当前权限和类型获取公司的部门和小组
	 * 
	 * @param type
	 *            dsyy 电商 zjsyy 转介绍
	 * @return
	 */
	@GetMapping("/get_base_info")
	public ResultInfo getStaffMarsInfo(String type) {
		List<GroupsInfoVO> list = groupService.getStaffMarsInfo(type, getCurrentLoginStaff().getId(),
				getCurrentLoginStaff().getCompanyId());
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, list);
	}

    /**
     *  根据权限和类型获取公司的部门列表
     * @param type
     * @return
     */
    @GetMapping("/get_dept_list")
    public ResultInfo getDeptListByType(String type){
        StaffPO staffPO = getCurrentLoginStaff();
        //获取该员工权限所能查看的该类型的所有部门
        List<GroupsInfoVO> list = schedulingService.getDeptListByType(type,staffPO.getCompanyId(),staffPO.getId());
        //过滤当前在线人数和接单数
		list= schedulingService.filterDeptList(list,staffPO.getCompanyId(),staffPO.getId());
        return ResultInfoUtil.success(list);
    }

    /**
     * 根据权限获取该部门下的所有小组
     * @param groupId
     * @return
     */
    @GetMapping("/get_group_list")
    public ResultInfo getGroupListByDept(String groupId){

    	StaffPO staffPO = getCurrentLoginStaff();
        List<GroupsInfoVO> list = schedulingService.getGroupListByDept(groupId,staffPO.getCompanyId(),staffPO.getId());
        //过滤当前在线人数和接单数
		list = schedulingService.filterGroupList(list,groupId,staffPO.getCompanyId());
        return ResultInfoUtil.success(list);
    }

    /**
     * 获取网销排班组员工列表
     * @return
     */
    @GetMapping("/get_group_staff_list")
    public ResultInfo getGroupStaffList(String groupId){
        List<StaffMarsDTO> staffList = staffService.getGroupStaffsDetail(getCurrentLoginStaff().getCompanyId(),groupId);
        return ResultInfoUtil.success(staffList);
    }

	/**
	 * 编辑网销排班人员设置
	 * 
	 * @param staffMarsDTO
	 * @return
	 */
	@PostMapping("/edit")
	public ResultInfo editStaffMars(@RequestBody StaffMarsDTO staffMarsDTO) {
		// 获取当前登录用户
		StaffPO currentLoginStaff = getCurrentLoginStaff();
		// 设置cid
		staffMarsDTO.setCompanyId(currentLoginStaff.getCompanyId());
		staffMarsDTO.setOperaId(currentLoginStaff.getId());
		staffMarsDTO.setOperaName(currentLoginStaff.getNickName());
		// 对象参数去空
		ObjectUtil.objectStrParamTrim(staffMarsDTO);
		schedulingService.editStaffMars(staffMarsDTO);

		return ResultInfoUtil.success();
	}

}
