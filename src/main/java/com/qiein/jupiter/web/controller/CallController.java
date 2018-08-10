package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CallCustomerPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.repository.DstgGoldDataReportsDao;
import com.qiein.jupiter.web.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private DstgGoldDataReportsDao dstgGoldDataReportsDao;
    /**
     * 添加客服
     * */
    @RequestMapping("/add_customer")
    public ResultInfo addCustomer(@RequestBody CallCustomerPO callCustomerPO){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.addCustomer(staffPO,callCustomerPO);
        return ResultInfoUtil.success();
    }

    /**
     * 更改客服信息
     * */
    @RequestMapping("/edit_customer")
    public ResultInfo editCustomer(@RequestBody CallCustomerPO callCustomerPO){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.editCustomer(staffPO,callCustomerPO);
        return ResultInfoUtil.success();
    }

    /**
     * 客服列表
     * */
    @RequestMapping("/customer_list")
    public ResultInfo customerList(){
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(callService.customerList(staffPO));
    }

    /**
     * 实例列表
     * */
    @RequestMapping("/instance_list")
    public ResultInfo instanceList(){
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(callService.instanceList(staffPO));
    }

    /**
     * 双呼
     * */
    @RequestMapping("/start_back_2_back_call")
    public ResultInfo startBack2BackCall(String kzId,String caller, String callee){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.startBack2BackCall(kzId,caller,callee,staffPO);
        return ResultInfoUtil.success();
    }

}
