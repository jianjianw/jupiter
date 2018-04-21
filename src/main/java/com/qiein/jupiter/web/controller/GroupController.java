package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import com.qiein.jupiter.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * group api
 */
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/get_company_all_dept_list")
    public ResultInfo getCompanyAllDeptlist() {
        //获取当前登录账户
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
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        groupPO.setCompanyId(currentLoginStaff.getCompanyId());
        //参数去trim
        ObjectUtil.objectStrParamTrim(groupPO);
        groupService.update(groupPO);
        return ResultInfoUtil.success(TipMsgConstant.UPDATE_SUCCESS);
    }

    /**
     * 新增
     *
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@Validated @RequestBody GroupPO groupPO) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        groupPO.setCompanyId(currentLoginStaff.getCompanyId());
        //参数去trim
        ObjectUtil.objectStrParamTrim(groupPO);
        groupService.insert(groupPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam int id) {
        if (id == 0) {
            throw new RException(ExceptionEnum.ID_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        groupService.delete(id, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.DELETE_SUCCESS);
    }

    /**
     * 获取当前公司所有的下属部门及员工
     */
    @GetMapping("/get_all_dept_and_staff")
    public ResultInfo getAllDeptAndStaff() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(groupService.getAllDeptAndStaff(currentLoginStaff.getCompanyId()));
    }

}
