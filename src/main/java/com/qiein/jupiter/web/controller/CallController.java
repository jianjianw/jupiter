package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
@RestController
@RequestMapping("/call")
public class CallController extends BaseController{
    @Autowired
    private CallService callService;
    /**
     * 双呼
     * */
    @RequestMapping("/start_back_2_back_call")
    public ResultInfo startBack2BackCall(String caller, String callee){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.startBack2BackCall(caller,callee,staffPO);
        return ResultInfoUtil.success();
    }

}
