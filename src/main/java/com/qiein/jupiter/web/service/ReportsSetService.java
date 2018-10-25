package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: shiTao
 */
public interface ReportsSetService {

    /**
     * 获取报表定义
     *
     * @return
     */
    JSONObject getDefineSet(int companyId);

    /**
     * 修改报表定义
     */
    int updateDefineSet(int companyId, JSONObject set);

    /**
     * 获取电商推广 来源数据统计 表头
     *
     * @return
     */
    JSONObject getR1ShowTitleSet(int companyId);

    /**
     * 修改电商推广 来源数据统计 表头
     */
    int updateR1ShowTitleSet(int companyId, JSONObject set);
}
