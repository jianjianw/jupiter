package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.AppolloUrlConst;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CallCustomerPO;
import com.qiein.jupiter.web.entity.po.CallPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.repository.DstgGoldDataReportsDao;
import com.qiein.jupiter.web.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Value("${apollo.baseUrl}")
    private String appoloBaseUrl;

    /**
     * 获取通话实例id
     * */
    @RequestMapping("/get_instance_id")
    public ResultInfo getInstanceId(){
        String companyInstaceId = getCompanyInstaceId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(companyInstaceId);
    }


    /**
     * 根据公司id获取通话实例id
     * */
    private String getCompanyInstaceId(int companyId){
        String sign = MD5Util.getApolloMd5(String.valueOf(companyId));
        String instaceJson = HttpClient
                // 请求方式和请求url
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_CALL_INSTANCE))
                // post提交json
                .queryString("companyId", companyId)
                .queryString("sign", sign)
                .asString();
        List<CallPO> callPOS = JSONObject.parseArray(JSONObject.parseObject(instaceJson).get("data").toString(), CallPO.class);
        if(CollectionUtils.isEmpty(callPOS)){
            return "";
        }
        return callPOS.get(0).getInstanceId();
    }

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
     * 删除客服
     * */
    @RequestMapping("/del_customer")
    public ResultInfo delCustomer(Integer id){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.delCustomer(id,staffPO);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
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
     * 获取录音列表
     * */
    @RequestMapping("/get_recording")
    public ResultInfo getRecording(String caller,Integer page,Integer pageSize,Integer startTime,String instanceId){
        StaffPO staffPO = getCurrentLoginStaff();
        JSONObject recording = callService.getRecording(caller, staffPO,page,pageSize,startTime,instanceId);
        return ResultInfoUtil.success(recording);
    }


    /**
     * 获取录音
     * */
    @RequestMapping("/get_recording_file")
    public ResultInfo getRecordingFile(String fileName,String instanceId){
        StaffPO staffPO = getCurrentLoginStaff();
        JSONObject jsonObject = callService.getRecordingFile(fileName, staffPO,instanceId);
        return ResultInfoUtil.success(jsonObject);
    }


    /**
     * 双呼
     * */
    @RequestMapping("/start_back_2_back_call")
    public ResultInfo startBack2BackCall(String kzId,String caller, String callee,Integer callId){
        StaffPO staffPO = getCurrentLoginStaff();
        callService.startBack2BackCall(kzId,caller,callee,staffPO,callId);
        return ResultInfoUtil.success();
    }

}
