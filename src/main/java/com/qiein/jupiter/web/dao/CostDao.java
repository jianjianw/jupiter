package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CostLogPO;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.po.ForDayPO;
import com.qiein.jupiter.web.entity.vo.CostShowVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CostDao {
    /**
     * 获取花费页面信息
     * @param month
     * @param companyId
     * @return
     */
     List<CostPO> costList(@Param("month") String month, @Param("companyId") Integer companyId,@Param("staffId")Integer staffId,@Param("sourceIds")String sourceIds);
    /**
     * 修改花费的返利率
     * @param srcIds
     * @param start
     * @param end
     * @param rate
     * @param companyId
     */
    void editRate(@Param("srcIds") String srcIds,@Param("start") Integer start, @Param("end")Integer end,@Param("rate") BigDecimal rate, @Param("companyId")Integer companyId);
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
    /**
     * 获取指定时间段内每天的时间戳
     */
    List<ForDayPO> getDay(@Param("start")Integer start,@Param("end") Integer end);
    /**
     * 获取数据库已存在渠道下每天的数据
     */
    List<ForDayPO> getSrcByDay(@Param("srcIds") String srcIds,@Param("start") Integer start, @Param("end")Integer end, @Param("companyId")Integer companyId);

    /**
     * 添加数据库暂无 的每个渠道下某天的返利率
     * @param list
     */
    void insertRate(List<ForDayPO> list);
    /**
     * 根据花费日期和渠道 查询是否存在该数据
     */
    List<CostPO> getCostByDayAndSrc(CostPO costPO);

}
