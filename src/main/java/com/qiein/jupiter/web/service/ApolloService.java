package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.VerifyParamDTO;

/**
 * 阿波罗接口
 *
 * @Author: shiTao
 */
public interface ApolloService {
    /**
     * 根据公司ID 和用户ID 获取用户验证信息
     *
     * @param companyId
     * @param staffId
     * @return
     */
    VerifyParamDTO getUserVerifyInfoByUidCid(int companyId, int staffId, String ip);

    /**
     * 获取Apollo 地址
     */
    void getApolloIp();

    /**
     * 从apollo 获取 CRM 地址
     */
    String getCrmUrlByCidFromApollo(int companyId);
}
