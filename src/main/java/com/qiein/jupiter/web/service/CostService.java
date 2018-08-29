package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.CostLogPO;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;

import java.math.BigDecimal;
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
    List<CostShowVO> costList(String month, Integer companyId,Integer staffId);

    /**
     * 修改花费的返利率
     * @param srcIds
     * @param start
     * @param end
     * @param rate
     * @param companyId
     */
    void editRate(String srcIds, Integer start, Integer end, BigDecimal rate,Integer companyId);
    /**
     * 添加花费
     * @param costPO
     */
    int insert(CostPO costPO);

    /**
     * 修改花费
     */
    void editCost(CostPO costPO,CostLogPO costLogPO);

}
