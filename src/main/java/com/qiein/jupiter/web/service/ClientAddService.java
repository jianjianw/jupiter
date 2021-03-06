package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.PlatAddClientInfoVO;

public interface ClientAddService {

    /**
     * 添加电商客资
     *
     * @param clientVO
     * @param staffPO
     */
    void addDsClient(ClientVO clientVO, StaffPO staffPO);

    /**
     * 添加转介绍客资
     *
     * @param clientVO
     * @param staffPO
     */
    void addZjsClient(ClientVO clientVO, StaffPO staffPO);

    /**
     * 功能描述:
     * 添加外部转介绍客资
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    void addOutZjsClient(ClientVO clientVO);

    /**
     * 添加自然入客客资
     *
     * @param clientVO
     * @param staffPO
     */
    void addMsClient(ClientVO clientVO, StaffPO staffPO);

    /**
     * 批量录入信息解析
     *
     * @param text
     */
    JSONArray changeStrToInfo(String text);

    /**
     * 批量录入
     *
     * @param list
     */
    JSONObject batchAddDsClient(String list, Integer channelId, Integer sourceId, Integer shopId, Integer typeId, StaffPO staffPO,
                                String adId, String adAddress, String groupId, Integer appointId, Integer zxStyle, Integer yxLevel,
                                Integer ysRange, Integer marryTime, String address);

    /**
     * 发送重复录入消息
     *
     * @param kzId
     * @param staffPO
     */
    void pushRepeatMsg(String kzId, StaffPO staffPO);


    /**
     * 手机端录入客资
     */
    void addDingClientInfo(ClientVO clientVO);


}
