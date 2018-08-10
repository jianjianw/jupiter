package com.qiein.jupiter.web.service;

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
     * */
    void startBack2BackCall(String kzId,String caller, String callee, StaffPO staffPO);
}
