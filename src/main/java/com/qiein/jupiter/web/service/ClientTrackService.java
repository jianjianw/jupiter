package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

public interface ClientTrackService {

    /**
     * 批量删除客资
     *
     * @param kzIds
     * @param staffPO
     */
    void batchDeleteKzList(String kzIds, StaffPO staffPO);


}
