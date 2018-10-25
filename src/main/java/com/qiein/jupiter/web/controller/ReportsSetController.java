package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.service.ReportsSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报表设置
 *
 * @Author: shiTao
 */
@RestController
@RequestMapping("/reports_set")
public class ReportsSetController extends BaseController {

    @Autowired
    private ReportsSetService reportsSetService;

    /**
     * 获取报表定义
     *
     * @return
     */
    @GetMapping("/get_define_set")
    public ResultInfo getDefineSet() {
        int companyId = getCurrentLoginStaff().getCompanyId();
        return ResultInfoUtil.success(reportsSetService.getDefineSet(companyId));
    }

    /**
     * 修改报表定义
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/update_define_set")
    public ResultInfo updateDefineSet(@RequestBody JSONObject jsonObject) {
        int companyId = getCurrentLoginStaff().getCompanyId();
        return ResultInfoUtil.success(reportsSetService.updateDefineSet(companyId, jsonObject));
    }

    /**
     * 获取电商推广来源数据统计的显示表头
     *
     * @return
     */
    @GetMapping("/get_r1_show_title_set")
    public ResultInfo getR1ShowTitleSet() {
        int companyId = getCurrentLoginStaff().getCompanyId();
        return ResultInfoUtil.success(reportsSetService.getDefineSet(companyId));
    }

    /**
     * 修改电商推广来源数据统计的表头
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/update_r1_show_title_set")
    public ResultInfo updateR1ShowTitleSet(@RequestBody JSONObject jsonObject) {
        int companyId = getCurrentLoginStaff().getCompanyId();
        return ResultInfoUtil.success(reportsSetService.updateR1ShowTitleSet(companyId, jsonObject));
    }


}
