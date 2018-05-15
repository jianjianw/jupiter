package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ClientStatusVO;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public interface ClientService {

    /**
     * 编辑性别
     * @param clientStatusVO
     */
    void editClientSex(ClientStatusVO clientStatusVO);

    /**
     * 编辑微信标识
     * @param clientStatusVO
     */
    void editClientWCFlag(ClientStatusVO clientStatusVO);
}
