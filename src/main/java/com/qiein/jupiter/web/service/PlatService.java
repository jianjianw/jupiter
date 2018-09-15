package com.qiein.jupiter.web.service;

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

}
