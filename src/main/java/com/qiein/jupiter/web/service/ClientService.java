package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.ClientVO;

/**
 * Created by Administrator on 2018/5/15 0015.
 */
public interface ClientService {

    /**
     * 编辑性别
     * @param clientVO
     */
    void editClientSex(ClientVO clientVO);

    /**
     * 编辑微信标识
     * @param clientVO
     */
    void editClientWCFlag(ClientVO clientVO);
}
