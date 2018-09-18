package com.qiein.jupiter.web.controller;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志
 *
 * @Author: shiTao
 */
@RestController
@RequestMapping("/sys_log")
public class SystemLogController extends BaseController {

    @Autowired
    private SystemLogService systemLogService;

    /**
     * 根据日志类型查询数据
     */
    @GetMapping("/get_sys_log_by_type")
    public ResultInfo getLogByType(int pageNum, int pageSize, int typeId, int startTime, int endTime) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        PageInfo<SystemLog> logByType = systemLogService.getLogByType(pageNum, pageSize, currentLoginStaff.getCompanyId(), typeId, startTime, endTime);
        return ResultInfoUtil.success(logByType);
    }

    /**
     * 网销排班分配日志
     */
    @GetMapping("/get_allot_log")
    public ResultInfo getAllotLog(@RequestParam Integer staffId){
        return ResultInfoUtil.success(systemLogService.getAllotLog(getCurrentLoginStaff().getCompanyId(),staffId));
    }
}
