package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.SourceTypeEnum;
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
        countSaleAvg(reportsList);

        //统计总花费
        countTotalSpend(reportsList, reportsParamVO);

        //统计ROI
        countRoi(reportsList);

        //统计合计并赋予中文名称
        countAll(reportsList);

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
        baseSql.append(getWhereSql());
        baseSql.append(" GROUP BY info.SRCTYPE ");
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
        baseSql.append(getWhereSql());

        baseSql.append(" GROUP BY info.SRCTYPE ");

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
        baseSql.append(getWhereSql());

        baseSql.append(" GROUP BY info.SRCTYPE ");

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
    private void countSaleAvg(List<SourceOrderDataReportsVO> reportsList) {
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
    private void countRoi(List<SourceOrderDataReportsVO> reportsList) {
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            //套系总金额/花费
            String saleAvg = reportsVO.getTotalSpend() == 0 ? "0" :
                    NumUtil.keep2Point(reportsVO.getTxTotalAmount() / reportsVO.getTotalSpend());
            reportsVO.setRoi(saleAvg);
        }
    }

    /**
     * 统计各种率
     */
    private void countRate(List<SourceOrderDataReportsVO> reportsList) {
        //TODO 是否需要？
    }

    /**
     * 统计合计 并且设置中文名称
     */
    private void countAll(List<SourceOrderDataReportsVO> reportsList) {
        int orderAll = 0;
        int txAll = 0;
        int zysAll = 0;
        double saleAvgAll = 0.0;
        int costAll = 0;
        double roiAll = 0.0;
        for (SourceOrderDataReportsVO reportsVO : reportsList) {
            //总订单
            orderAll += reportsVO.getOrderAmount();
            //总套系
            txAll += reportsVO.getTxTotalAmount();
            //总已收
            zysAll += reportsVO.getTotalYsAmount();
            //总销售均价
            saleAvgAll += Double.valueOf(reportsVO.getSaleAvg());
            //总花费
            costAll += reportsVO.getTotalSpend();
            //roi
            roiAll += Double.valueOf(reportsVO.getRoi());
            //设置来源的中文
            reportsVO.setSrcName(SourceTypeEnum.switchName(reportsVO.getSrcType()));
        }
        //总的合计
        SourceOrderDataReportsVO allReportsVO = new SourceOrderDataReportsVO();
        allReportsVO.setOrderAmount(orderAll);
        allReportsVO.setTxTotalAmount(txAll);
        allReportsVO.setTotalYsAmount(zysAll);
        allReportsVO.setSaleAvg(String.valueOf(saleAvgAll));
        allReportsVO.setTotalSpend(costAll);
        allReportsVO.setRoi(String.valueOf(roiAll));
        allReportsVO.setSrcType(-1);
        allReportsVO.setSrcName(CommonConstant.totalAll);
        reportsList.add(0, allReportsVO);
    }


    private String getWhereSql() {
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE info.COMPANYID = ? ")
                .append(" AND  info.ISDEL = 0 ")
                .append(" AND info.CREATETIME BETWEEN  ? AND ? ")
                .append(" AND info.GROUPID = :groupId ");
        return whereSql.toString();
    }


}
