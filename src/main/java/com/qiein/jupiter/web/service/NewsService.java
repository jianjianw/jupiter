package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.NewsTotalAmountAndFlag;

public interface NewsService {

    /**
     * 获取所有消息列表
     *
     * @param queryMapDTO
     * @return
     */
    PageInfo<?> getAllList(QueryMapDTO queryMapDTO, int uid, int cid);

    /**
     * 获取未读消息列表
     *
     * @param queryMapDTO
     * @return
     */
    PageInfo<?> getNotReadList(QueryMapDTO queryMapDTO, int uid, int cid);

    /**
     * 批量更新消息为已读
     *
     * @param msgIds
     */
    int batchUpdateNewsReadFlag(String msgIds, int staffId, int cid);

    /**
     * 获取各种类型未读消息的数量及是否存在未读
     */
    NewsTotalAmountAndFlag getNewsTotalAmountAndFlag(int uid, int cid);

    /**
     * 设置所有的消息为已读
     *
     * @param uid
     * @param cid
     * @return
     */
    int setAllNewIsRead(int uid, int cid);

    /**
     * 推送闪信信息，，催一次，崔二次
     *
     * @param kzId
     * @param toStaffId
     * @param msg
     * @param opera
     */
    void pushInfoNewsInApp(String kzId, int toStaffId, String msg, StaffPO opera);

}
