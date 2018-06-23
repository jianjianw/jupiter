package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;

import java.util.List;
import java.util.Map;

/**
 * 电商渠道花费
 * Author xiangliang 2018/6/22
 */
public interface CostService {
    /**
     * 获取花费页面信息
     * @param month
     * @param companyId
     * @return
     */
    List<Map> costList(String month, Integer companyId);

    /**
     * 添加花费
     * @param costPO
     */
    void insert(CostPO costPO);

    /**
     * 修改花费
     */
    void editCost(CostPO costPO);
}