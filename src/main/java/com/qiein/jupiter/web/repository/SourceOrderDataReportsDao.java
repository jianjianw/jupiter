package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.SourceOrderDataReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        countOrderAmount(reportsList, reportsParamVO);

        //统计套系总金额
        countTxTotalAmount(reportsList, reportsParamVO);

        //统计总已收金额
        countTotalYsAmount(reportsList, reportsParamVO);

        //统计销售均价
        countSaleAvg(reportsList, reportsParamVO);

        //统计总花费
        countTotalSpend(reportsList, reportsParamVO);

        //统计ROI
        countRoi(reportsList, reportsParamVO);
        return reportsList;
    }


    /**
     * 统计订单总量
     */
    private void countOrderAmount(final List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,COUNT(*) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ");
        baseSql.append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = ? ")
                .append(" AND info.CREATETIME BETWEEN ? AND ? ")
                .append(" GROUP BY info.SRCTYPE ");
        //处理结果集
        jdbcTemplate.query(baseSql.toString(),
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        SourceOrderDataReportsVO reportsVO = new SourceOrderDataReportsVO();
                        reportsVO.setSrcType(rs.getInt("SRCTYPE"));
                        reportsVO.setOrderAmount(rs.getInt("COUNT"));
                        reportsList.add(reportsVO);
                    }
                },
                cid, reportsParamVO.getStart(), reportsParamVO.getEnd());

    }

    /**
     * 统计套系总金额
     */
    private void countTxTotalAmount(List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        String detailTableName = DBSplitUtil.getDetailTabName(cid);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,COUNT(det.AMOUNT) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN ").append(detailTableName).append(" det ")
                .append(" ON info.KZID = det.KZID AND info.COMPANYID = det.COMPANYID ");
        baseSql.append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = ? ")
                .append(" AND info.CREATETIME BETWEEN ? AND ? ")
                .append(" GROUP BY info.SRCTYPE ");

        final Map<Integer, Integer> map = new HashMap<>();
        jdbcTemplate.query(baseSql.toString(),
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        map.put(rs.getInt("SRCTYPE"), rs.getInt("COUNT"));
                    }
                },
                cid, reportsParamVO.getStart(), reportsParamVO.getEnd());
        //遍历赋值
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            int txTotalAmount = map.get(reportsVO.getSrcType());
            reportsVO.setTxTotalAmount(txTotalAmount);
        }

    }


    /**
     * 统计总已收金额
     */
    private void countTotalYsAmount(List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        String detailTableName = DBSplitUtil.getDetailTabName(cid);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,COUNT(det.STAYAMOUNT) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN ").append(detailTableName).append(" det ")
                .append(" ON info.KZID = det.KZID AND info.COMPANYID = det.COMPANYID ");
        baseSql.append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = ? ")
                .append(" AND info.CREATETIME BETWEEN ? AND ? ")
                .append(" GROUP BY info.SRCTYPE ");

        final Map<Integer, Integer> map = new HashMap<>();
        jdbcTemplate.query(baseSql.toString(),
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        map.put(rs.getInt("SRCTYPE"), rs.getInt("COUNT"));
                    }
                },
                cid, reportsParamVO.getStart(), reportsParamVO.getEnd());
        //遍历赋值
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            int ysAmount = map.get(reportsVO.getSrcType());
            reportsVO.setTotalYsAmount(ysAmount);
        }
    }


    /**
     * 销售均价
     */
    private void countSaleAvg(List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            String saleAvg = NumUtil.keep2Point(reportsVO.getTxTotalAmount() / reportsVO.getOrderAmount());
            reportsVO.setSaleAvg(saleAvg);
        }

    }

    /**
     * 总花费
     */
    private void countTotalSpend(List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        int cid = reportsParamVO.getCompanyId();

        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT source.TYPEID,SUM(cost.cost) COST FROM hm_crm_cost cost ");

        baseSql.append(" LEFT JOIN hm_crm_source source ")
                .append(" ON source.ID = cost.SRCID ");
        baseSql.append(" WHERE  cost.COMPANYID = ? AND cost.CREATETIME  BETWEEN ? AND ? ");
        baseSql.append(" GROUP BY source.TYPEID ");

        final Map<Integer, Integer> map = new HashMap<>();
        jdbcTemplate.query(baseSql.toString(),
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        map.put(rs.getInt("TYPEID"), rs.getInt("COST"));
                    }
                },
                cid, reportsParamVO.getStart(), reportsParamVO.getEnd());
        //遍历赋值
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            Integer cost = map.get(reportsVO.getSrcType());
            if (cost == null) cost = 0;
            reportsVO.setTotalSpend(cost);
        }
    }

    /**
     * ROI
     */
    private void countRoi(List<SourceOrderDataReportsVO> reportsList, ReportsParamVO reportsParamVO) {
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            //套系总金额/花费
            String saleAvg;
            if (reportsVO.getTotalSpend() == 0) {
                saleAvg = "0";
            } else {
                saleAvg = NumUtil.keep2Point(reportsVO.getTxTotalAmount() / reportsVO.getTotalSpend());
            }
            reportsVO.setRoi(saleAvg);
        }
    }


}
