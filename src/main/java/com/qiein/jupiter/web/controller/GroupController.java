package com.qiein.jupiter.web.controller;

import java.util.HashMap;
import java.util.List;

import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
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
import com.qiein.jupiter.enums.TipMsgEnum;
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

import java.util.Map;

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
        StaffPO staff = getCurrentLoginStaff();
        List<GroupVO> list = groupService.getCompanyAllDeptList(staff.getCompanyId());
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
        StaffPO staff = getCurrentLoginStaff();
        groupPO.setCompanyId(staff.getCompanyId());
        // 参数去trim
        ObjectUtil.objectStrParamTrim(groupPO);
        RequestInfoDTO requestInfo = getRequestInfo();
        GroupPO groupPO1=groupService.getGroupById(staff.getCompanyId(),groupPO.getGroupId());
        Map<String,String> map=new HashMap<>();
        map.put(groupPO1.getGroupName(),groupPO.getGroupName());
        map.put(groupPO1.getChiefNames(),groupPO.getChiefNames());
        groupService.update(groupPO);
        GroupPO groupPO2=groupService.getGroupById(staff.getCompanyId(),groupPO.getGroupId());
        map.put(groupPO1.getChiefNames(),groupPO2.getChiefNames());
        try {
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_GROUP, requestInfo.getIp(), requestInfo.getUrl(), staff.getId(),
                    staff.getNickName(), SysLogUtil.getEditLog(SysLogUtil.LOG_SUP_GROUP, SysLogUtil.LOG_SUP_GROUP,map),
                    staff.getCompanyId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
        }
        // 同步修改分配表中的客服组名
//        ShopChannelGroupService.updateGroupNameById(groupPO.getGroupName(), groupPO.getGroupId(),
//                groupPO.getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 新增
     *
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@Validated @RequestBody GroupPO groupPO) {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        RequestInfoDTO requestInfo = getRequestInfo();
        groupPO.setCompanyId(staff.getCompanyId());
        // 参数去trim
        ObjectUtil.objectStrParamTrim(groupPO);
        groupService.insert(groupPO);
        // 日志记录
        SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_GROUP, requestInfo.getIp(), requestInfo.getUrl(), staff.getId(),
                staff.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_GROUP, groupPO.getGroupName(),
                groupPO.getGroupType(), groupPO.getChiefNames()),
                staff.getCompanyId());
        logService.addLog(log);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@Id Integer id) {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        RequestInfoDTO requestInfo = getRequestInfo();
        GroupPO groupPO = groupService.delete(id, staff.getCompanyId());
        // 日志记录
        logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_GROUP, requestInfo.getIp(), requestInfo.getUrl(), staff.getId(),
                staff.getNickName(), SysLogUtil.getRemoveLog(SysLogUtil.LOG_SUP_GROUP, groupPO.getGroupName()),
                staff.getCompanyId()));
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 获取当前公司所有的下属部门及员工
     */
    @GetMapping("/get_all_dept_and_staff")
    public ResultInfo getAllDeptAndStaff() {
        return ResultInfoUtil.success(groupService.getAllDeptAndStaff(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 根据角色，权限，获取小组及人员下拉
     */
    @GetMapping("/get_group_staff_by_role")
    public ResultInfo getGroupStaffByRole(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        if (RoleConstant.CWZX.equals(role)) {
            role = RoleConstant.MSJD;
        }
        return ResultInfoUtil.success(groupService.getGroupStaffByType(staff.getCompanyId(), staff.getId(), role));
    }

    /**
     * 获取邀约客服小组及人员
     */
    @GetMapping("/get_dsyy_group_staff_list")
    public ResultInfo getDsyyGroupStaffList() {
        return ResultInfoUtil.success(groupService.getDsyyGroupStaffList(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 根据角色，获取对应的全部小组人员
     */
    @GetMapping("/get_all_group_staff_by_role")
    public ResultInfo getAllGroupStaffByRole(@NotEmptyStr String role) {
        return ResultInfoUtil.success(groupService.getAllGroupStaffByRole(getCurrentLoginStaff().getCompanyId(), role));
    }

    /**
     * 获取门市下面的所有人员列表
     */
    @GetMapping("/get_msjd_staff_list")
    public ResultInfo getMsjdStaffList() {
        return ResultInfoUtil.success(groupService.getMsjdStaffList(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 根据角色，获取在线小组人员，用于分配
     */
    @GetMapping("/get_online_staff_list_by_role")
    public ResultInfo getOnLineStaffListByRole(@NotEmptyStr @RequestParam("role") String role) {
        if (RoleConstant.DSCJ.equals(role) || RoleConstant.DSSX.equals(role)) {
            role = RoleConstant.DSYY;
        } else if (RoleConstant.DSYY.equals(role) || RoleConstant.ZJSYY.equals(role)
                || RoleConstant.MSJD.equals(role) || RoleConstant.CWZX.equals(role)) {
            role = RoleConstant.MSJD;
        } else if (RoleConstant.ZJSSX.equals(role)) {
            role = RoleConstant.ZJSYY;
        }
        return ResultInfoUtil.success(groupService.getOnLineStaffListByRole(getCurrentLoginStaff().getCompanyId(), role));
    }

    /**
     * 获取全公司小组人员列表
     */
    @GetMapping("/get_all_group_staff_list")
    public ResultInfo getAllGroupStaffList() {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(groupService.getAllGroupStaff(staff.getCompanyId()));
    }

    /**
     * 获取全公司小组
     */
    @GetMapping("/get_group_list_by_type")
    public ResultInfo getGroupListByType(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(groupService.getGroupListByType(staff.getCompanyId(), role));
    }

    /**
     * 获取公司下部门和小组
     */
    @GetMapping("/get_depart_group_list_by_type")
    public ResultInfo getDepartGroupListByType(@NotEmptyStr @RequestParam("role") String role) {
        // 获取当前登录账户
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(groupService.getDepartGroupListByType(staff.getCompanyId(), role));
    }

    @GetMapping("/priority")
    public ResultInfo editPriority( Integer fId, Integer fPriority, Integer sId, Integer sPriority) {
        groupService.editProiority(fId, fPriority, sId, sPriority, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

}