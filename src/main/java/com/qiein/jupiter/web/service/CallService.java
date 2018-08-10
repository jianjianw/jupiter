package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CallCustomerPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

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
    void startBack2BackCall(String kzId,String caller, String callee, StaffPO staffPO);

    /**
     * 添加客服
     * @param callCustomerPO
     * @param staffPO
     * */
    void addCustomer(StaffPO staffPO, CallCustomerPO callCustomerPO);
}
