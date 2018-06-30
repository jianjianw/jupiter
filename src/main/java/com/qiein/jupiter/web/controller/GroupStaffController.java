package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.GroupStaffPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.GroupStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * FileName: GroupStaffController
 *
 * @author: yyx
 * @Date: 2018-6-27 10:05
 */
@RestController
@RequestMapping("/group_staff")
public class GroupStaffController extends BaseController{
    @Autowired
    private GroupStaffService groupStaffService;
    /**
     *  插入部门员工关系
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody JSONObject jsonObject){
        StaffPO staffPO = getCurrentLoginStaff();


        groupStaffService.insert(jsonObject,staffPO);
        return ResultInfoUtil.success();
    }

    /**
     * 移除
     * */
    @PostMapping("/remove")
    public ResultInfo remove(@RequestBody GroupStaffPO groupStaffPO){
        StaffPO staffPO = getCurrentLoginStaff();
        groupStaffPO.setCompanyId(staffPO.getCompanyId());
        groupStaffService.remove(groupStaffPO);
        return ResultInfoUtil.success();
    }
}
