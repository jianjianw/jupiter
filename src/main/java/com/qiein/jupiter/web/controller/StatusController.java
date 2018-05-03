package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
@Validated
public class StatusController extends BaseController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/get_company_status_list")
    public ResultInfo getCompanyStatusList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(statusService.getCompanyStatusList(currentLoginStaff.getCompanyId()));
    }

    @PostMapping("/edit_status")
    public ResultInfo editStatus(@RequestBody StatusPO statusPO) {
        if (NumUtil.isNull(statusPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        statusPO.setCompanyId(currentLoginStaff.getCompanyId());
        statusService.editStatus(statusPO);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }
}
