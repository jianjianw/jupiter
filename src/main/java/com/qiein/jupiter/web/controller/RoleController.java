package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.entity.vo.RoleVO;
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

    @GetMapping("/get_company_role_list")
    public ResultInfo getCompanyRolelist() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<RolePermissionVO> roleList = roleService.getCompanyAllRole(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(roleList);
    }

    @GetMapping("/add_role")
    public ResultInfo addRole(@NotEmpty @RequestParam("roleName") String roleName, @RequestParam("priority") int priority, @RequestParam("pmsIds") String pmsIds) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        roleService.insert(roleName, priority, pmsIds, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.ADD_ROLE_SUCCESS);
    }

    @GetMapping("/delete_role")
    public ResultInfo deleteRole(@NotEmpty @RequestParam("roleId") Integer roleId) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //TODO 检查该角色是否绑定人员，如果有提示不能删除
        roleService.delete(roleId, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.DELETE_ROLE_SUCCESS);
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
        return ResultInfoUtil.success(TipMsgConstant.EDIT_ROLE_SUCCESS);
    }

    @GetMapping("/get_role_select")
    public ResultInfo getRoleSelect() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        List<RolePO> roleList = roleService.getRoleSelect(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(roleList);
    }


}
