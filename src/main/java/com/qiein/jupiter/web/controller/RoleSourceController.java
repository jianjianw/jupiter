package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.service.RoleSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: RoleSourceController
 *
 * @author: yyx
 * @Date: 2018-7-18 18:48
 */
@RestController
@RequestMapping("/role_source")
public class RoleSourceController extends BaseController{
    @Autowired
    private RoleSourceService roleSourceService;

    @RequestMapping("insert")
    public ResultInfo insert(@RequestBody JSONObject jsonObject){
        int roleId = jsonObject.getIntValue("roleId");
        String sourceId = jsonObject.getString("sourceId");
        roleSourceService.insert(roleId,sourceId,getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }


}
