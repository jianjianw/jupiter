package com.qiein.jupiter.web.controller;

import java.util.List;

import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.RoleSourceService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.entity.vo.RoleVO;
import com.qiein.jupiter.web.service.RoleService;

/**
 * 角色权限
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;//角色业务层

    @Autowired
    private SystemLogService logService;//日志业务层

    @Autowired
    private RoleSourceService roleSourceService;//花费规则

    @GetMapping("/get_company_role_list")
    public ResultInfo getCompanyRoleList() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<RolePermissionVO> roleList = roleService.getCompanyAllRole(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(roleList);
    }

    @GetMapping("/add_role")
    public ResultInfo addRole(@NotEmptyStr @RequestParam("roleName") String roleName) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        int id = roleService.insert(roleName, currentLoginStaff.getCompanyId());
        RequestInfoDTO requestInfo = getRequestInfo();
        try {
            // 日志记录
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_ROLE, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_ROLE, roleName),
                    currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.ADD_ROLE_SUCCESS, id);
        }
        return ResultInfoUtil.success(TipMsgEnum.ADD_ROLE_SUCCESS, id);
    }

    @GetMapping("/role_used")
    public ResultInfo roleUsed(@Id @RequestParam("roleId") int roleId) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(roleService.roleUsed(currentLoginStaff.getCompanyId(), roleId));
    }

    @GetMapping("/delete_role")
    public ResultInfo deleteRole(@Id @RequestParam("roleId") Integer roleId) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        roleService.delete(roleId, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.DELETE_ROLE_SUCCESS);
    }

    @PostMapping("/edit_role")
    public ResultInfo editRole(@RequestBody RoleVO roleVO) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (NumUtil.isNull(roleVO.getRoleId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        roleVO.setCompanyId(currentLoginStaff.getCompanyId());
        roleService.update(roleVO);
        if (StringUtil.isNotEmpty(roleVO.getSourceIds())) {
            roleSourceService.insert(roleVO.getRoleId(), roleVO.getSourceIds(), getCurrentLoginStaff().getCompanyId());
        }
        return ResultInfoUtil.success(TipMsgEnum.EDIT_ROLE_SUCCESS);
    }

    @GetMapping("/get_role_select")
    public ResultInfo getRoleSelect() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<RolePO> roleList = roleService.getRoleSelect(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(roleList);
    }

    /**
     * 角色排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/edit_priority")
    public ResultInfo editRolePriority(@Id Integer fId, @Id Integer fPriority,
                                       @Id Integer sId, @Id Integer sPriority) {
        roleService.editProiority(fId, fPriority, sId, sPriority, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

}
