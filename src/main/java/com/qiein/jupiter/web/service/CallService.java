package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public interface CallService {
    /**
     * 外呼
     * @param caller
     * @param callee
     * */
    void startBack2BackCall(String caller, String callee, StaffPO staffPO);
}
