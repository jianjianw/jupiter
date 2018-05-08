package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
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
        return ResultInfoUtil.success(TigMsgEnum.ADD_ROLE_SUCCESS, id);
    }

    @GetMapping("/delete_role")
    public ResultInfo deleteRole(@Id @RequestParam("roleId") Integer roleId) {
        if (roleId == 0) {
            throw new RException(ExceptionEnum.ID_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        roleService.delete(roleId, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.DELETE_ROLE_SUCCESS);
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
        return ResultInfoUtil.success(TigMsgEnum.EDIT_ROLE_SUCCESS);
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
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/edit_priority")
    public ResultInfo editRolePriority (@Id Integer fId, @Id Integer fPriority,
                                        @Id Integer sId, @Id Integer sPriority){
        roleService.editProiority(fId,fPriority,sId,sPriority,getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

}
