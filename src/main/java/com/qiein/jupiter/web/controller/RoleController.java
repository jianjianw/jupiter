package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;//角色业务层

    @GetMapping("get_company_role_list")
    public ResultInfo getCompanyRolelist() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<RolePermissionVO> roleList = roleService.getCompanyAllRole(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(roleList);
    }

    @PostMapping("add_role")
    public ResultInfo addRole(@NotEmpty @RequestParam("roleName") String roleName, @NotEmpty @RequestParam("priority") Integer priority, @RequestParam("pmsIds") String pmsIds) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        roleService.insert(roleName, priority, pmsIds, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.ADD_ROLE_SUCCESS);
    }

    @PostMapping("delete_role")
    public ResultInfo deleteRole(@NotEmpty @RequestParam("roleId") Integer roleId) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        roleService.delete(roleId,currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.DELETE_ROLE_SUCCESS);
    }
}
