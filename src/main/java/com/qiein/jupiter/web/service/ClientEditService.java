package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONArray;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

/**
 * 编辑客资业务层
 */
public interface ClientEditService {

    /**
     * 电商推广修改客资
     *
     * @param clientVO
     * @param staffPO
     */
    void editClientByDscj(ClientVO clientVO, StaffPO staffPO);


    /**
     * 电商邀约修改客资
     *
     * @param clientVO
     * @param staffPO
     */
    void editClientByDsyy(ClientVO clientVO, StaffPO staffPO);

    /**
     * 主管纠错
     *
     * @param clientVO
     * @param staffPO
     */
    void editClientByCwzx(ClientVO clientVO, StaffPO staffPO);


}
