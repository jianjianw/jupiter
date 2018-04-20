package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;

public interface NewsService {
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
    int batchUpdateNewsReadFlag(String msgIds, int cid);

}
