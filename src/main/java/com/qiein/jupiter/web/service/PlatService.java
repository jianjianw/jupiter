package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.PlatPageVO;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import com.qiein.jupiter.web.entity.vo.SearchClientVO;

import java.util.List;

/**
 * 平台接口
 *
 * @Author: shiTao
 */
public interface PlatService {

    /**
     * 页面搜索客资
     *
     * @param companyId
     * @param key
     * @return
     */
    List<SearchClientVO> pageSearch(int companyId, String key);


    /**
     * 查看客资详情
     */
    JSONObject getClientInfoByKzid(QueryVO queryVO);

    /**
     * 查看删除客资
     */
    PlatPageVO getDelClient(QueryVO queryVO);

    /**
     * 页面查询重复客资
     */
    PlatPageVO getRepeatClientHsWeb(QueryVO queryVO);

    /**
     * 页面查询客资
     */
    PlatPageVO queryPageClientInfo(QueryVO queryVO);

    /**
     * 页面客资查询统计
     *
     * @param queryVO
     * @return
     */
    JSONObject queryPageClientInfoCount(QueryVO queryVO);
}
