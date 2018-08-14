package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.SourceOrderDataReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * （财务中心）渠道订单数据统计
 *
 * @Author: shiTao
 */
@Repository
public class SourceOrderDataReportsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 渠道订单数据统计
     */
    public List<SourceOrderDataReportsVO> getSourceOrderDataReports(ReportsParamVO reportsParamVO) {
        List<SourceOrderDataReportsVO> reportsList = new ArrayList<>();
        //统计订单总量
        countOrderAmount(reportsList);

        //统计套系总金额
        countTxTotalAmount(reportsList);

        //统计总已收金额
        countTotalYsAmount(reportsList);

        //统计销售均价
        countSaleAvg(reportsList);

        //统计总花费
        countTotalSpend(reportsList);
        return reportsList;
    }


    /**
     * 统计订单总量
     */
    private void countOrderAmount(List<SourceOrderDataReportsVO> reportsList) {
        StringBuffer selectSql=new StringBuffer();
    }

    /**
     * 统计套系总金额
     */
    private void countTxTotalAmount(List<SourceOrderDataReportsVO> reportsList) {
    }


    /**
     * 统计总已收金额
     */
    private void countTotalYsAmount(List<SourceOrderDataReportsVO> reportsList) {

    }


    /**
     * 销售均价
     */
    private void countSaleAvg(List<SourceOrderDataReportsVO> reportsList) {

    }

    /**
     * 总花费
     */
    private void countTotalSpend(List<SourceOrderDataReportsVO> reportsList) {

    }

    /**
     * ROI
     */
    private void countRoi(List<SourceOrderDataReportsVO> reportsList) {

    }
}
