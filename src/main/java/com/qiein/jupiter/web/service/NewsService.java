package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.vo.NewsTotalAmountAndFlag;

public interface NewsService {
    /**
     * 设置单条消息的已读状态
     *
     * @param id  消息Id
     * @param cid 公司id
     * @return
     */
    int updateNewsReadFlag(int id, int cid);

    /**
     * 获取所有消息列表
     *
     * @param queryMapDTO
     * @return
     */
    PageInfo getAllList(QueryMapDTO queryMapDTO, int uid, int cid);

    /**
     * 获取未读消息列表
     *
     * @param queryMapDTO
     * @return
     */
    PageInfo getNotReadList(QueryMapDTO queryMapDTO, int uid, int cid);

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

}
