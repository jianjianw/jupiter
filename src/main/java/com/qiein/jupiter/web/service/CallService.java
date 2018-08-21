package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.po.CallCustomerPO;
import com.qiein.jupiter.web.entity.po.CallPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public interface CallService {
    /**
     * 外呼
     * @param kzId
     * @param caller
     * @param callee
     * @param staffPO
     * */
    void startBack2BackCall(String kzId,String caller, String callee, StaffPO staffPO,Integer callId);

    /**
     * 添加客服
     * @param callCustomerPO
     * @param staffPO
     * */
    void addCustomer(StaffPO staffPO, CallCustomerPO callCustomerPO);

    /**
     * 修改客服
     * @param callCustomerPO
     * @param staffPO
     * */
    void editCustomer(StaffPO staffPO, CallCustomerPO callCustomerPO);

    /**
     * 获取客服列表
     * @param staffPO
     * @return
     * */
    List<CallCustomerPO> customerList(StaffPO staffPO);

    /**
     * 实例列表
     * @param staffPO
     * @return
     * */
    List<CallPO> instanceList(StaffPO staffPO);

    /**
     * 获取录音列表
     * */
    JSONObject getRecording(String caller, StaffPO staffPO,Integer page,Integer pageSize,Integer callId);

    /**
     * 获取录音文件
     * */
    JSONObject getRecordingFile(String fileName,StaffPO staffPO,Integer callId);

}
