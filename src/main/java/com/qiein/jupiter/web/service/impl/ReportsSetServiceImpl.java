package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.dao.ReportsSetDao;
import com.qiein.jupiter.web.service.ReportsSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报表设置
 *
 * @Author: shiTao
 */
@Service
public class ReportsSetServiceImpl implements ReportsSetService {
    @Autowired
    private ReportsSetDao reportsSetDao;


    /**
     * 获取报表定义
     *
     * @param companyId
     * @return
     */
    @Override
    public JSONObject getDefineSet(int companyId) {
        String defineSet = reportsSetDao.getDefineSet(companyId);
        System.out.println(defineSet);
        return JSONObject.parseObject(defineSet);
    }

    /**
     * 修改报表定义
     *
     * @param companyId
     * @param set
     * @return
     */
    @Override
    public int updateDefineSet(int companyId, JSONObject set) {
        return reportsSetDao.updateDefineSet(companyId, set.toString());
    }

    /**
     * 获取来源数据统计表头 显示
     *
     * @param companyId
     * @return
     */
    @Override
    public JSONObject getR1ShowTitleSet(int companyId) {

        String r1ShowTitleSet = reportsSetDao.getR1ShowTitleSet(companyId);

        return JSONObject.parseObject(r1ShowTitleSet);
    }

    /**
     * 修改来源数据统计表头 显示
     */
    @Override
    public int updateR1ShowTitleSet(int companyId, JSONObject set) {
        return reportsSetDao.updateR1ShowTitleSet(companyId, set.toString());
    }
}
