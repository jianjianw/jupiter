package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ClientStatusVO;

import java.util.HashMap;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public interface ClientService {

    /**
     * 编辑性别
     *
     * @param clientStatusVO
     */
    void editClientSex(ClientStatusVO clientStatusVO);

    /**
     * 编辑微信标识
     *
     * @param clientStatusVO
     */
    void editClientWCFlag(ClientStatusVO clientStatusVO);

    /**
     * 微信二位码扫描记录
     *
     * @param companyId
     * @param kzId
     */
    void scanWechat(int companyId, String kzId);

    /**
     * 根据状态筛选客资数量
     *
     * @param companyId
     * @return
     */
    HashMap<String, Integer> getKzNumByStatusId(int companyId);
}
