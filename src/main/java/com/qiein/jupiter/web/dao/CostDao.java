package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CostLogPO;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CostDao {
    /**
     * 获取花费页面信息
     * @param month
     * @param companyId
     * @return
     */
     List<CostShowVO> costList(@Param("month") String month, @Param("companyId") Integer companyId);

    /**
     * 添加花费
     * @param costPO
     */
    int insert(CostPO costPO);

    /**
     * 修改花费
     */
    void editCost(CostPO costPO);
    /**
     * 添加花费日志
     */
    void createCostLog(CostLogPO costLogPO);
}
