package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.enums.SourceTypeEnum;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务 月度订单数据统计
 *
 * @Author: shiTao
 */
@Repository
public class CwMonthOrderCountReportsDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 获取统计报表
     *
     * @param reportsParamVO
     */
    public JSONObject getCwMonthOrderCountReports(ReportsParamVO reportsParamVO) {
        //获取月度各个区订单量
        List<JSONObject> monthOrderCount = getMonthOrderCount(reportsParamVO);
        //获取月度成交套系总金额
        List<JSONObject> monthTxCount = getMonthTxCount(reportsParamVO);
        //获取月度成交已收总金额
        List<JSONObject> monthYsCount = getMonthYsCount(reportsParamVO);
        JSONObject reports = new JSONObject();
        reports.put("orderCountReports", monthOrderCount);
        reports.put("txCountReports", monthTxCount);
        reports.put("ysCountReports", monthYsCount);
        return reports;
    }


    /**
     * 月度 各个渠道订单量汇总
     *
     * @param reportsParamVO
     * @return
     */
    private List<JSONObject> getMonthOrderCount(ReportsParamVO reportsParamVO) {
        List<JSONObject> reportsList = new ArrayList<>();
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> stringObjectMap = initBaseParamsMap(reportsParamVO);
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,FROM_UNIXTIME( info.SUCCESSTIME, '%d' ) TIME, COUNT( * ) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ");
        //拼接where
        baseSql.append(getBaseWhereSql());
        //拼接groupBy
        baseSql.append(getBaseGroupBySql());
        //执行查询并处理
        handleChannelDayData(reportsList, baseSql.toString(), stringObjectMap);

        return reportsList;
    }

    /**
     * 获取月度成交套系总金额
     *
     * @param reportsParamVO
     * @return
     */
    private List<JSONObject> getMonthTxCount(ReportsParamVO reportsParamVO) {
        List<JSONObject> reportsList = new ArrayList<>();
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        String detailTableName = DBSplitUtil.getDetailTabName(cid);

        //封装参数
        Map<String, Object> stringObjectMap = initBaseParamsMap(reportsParamVO);
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,FROM_UNIXTIME( info.SUCCESSTIME, '%d' ) TIME, SUM( det.AMOUNT ) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN ").append(detailTableName).append(" det ")
                .append(" ON info.KZID = det.KZID AND info.COMPANYID = det.COMPANYID ");
        //拼接where
        baseSql.append(getBaseWhereSql());
        //拼接groupBy
        baseSql.append(getBaseGroupBySql());
        //执行查询并处理
        handleChannelDayData(reportsList, baseSql.toString(), stringObjectMap);

        return reportsList;
    }


    /**
     * 获取月度成交已收总金额
     *
     * @param reportsParamVO
     * @return
     */
    private List<JSONObject> getMonthYsCount(ReportsParamVO reportsParamVO) {
        List<JSONObject> reportsList = new ArrayList<>();
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        String detailTableName = DBSplitUtil.getDetailTabName(cid);

        //封装参数
        Map<String, Object> stringObjectMap = initBaseParamsMap(reportsParamVO);
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,FROM_UNIXTIME( info.SUCCESSTIME, '%d' ) TIME, SUM( det.STAYAMOUNT ) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN ").append(detailTableName).append(" det ")
                .append(" ON info.KZID = det.KZID AND info.COMPANYID = det.COMPANYID ");
        //拼接where
        baseSql.append(getBaseWhereSql());
        //拼接groupBy
        baseSql.append(getBaseGroupBySql());
        //执行查询并处理
        handleChannelDayData(reportsList, baseSql.toString(), stringObjectMap);

        return reportsList;
    }

    /**
     * 处理 每天数据
     */
    private void handleChannelDayData(List<JSONObject> reportsList, String sql, Map<String, Object> stringObjectMap) {
        //要处理的
        final Map<Integer, List<JSONObject>> rMap = new HashMap<>();
//
        namedJdbc.query(sql, stringObjectMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                Integer srcType = resultSet.getInt("SRCTYPE");
                JSONObject rJson = new JSONObject();
                rJson.put("srcType", srcType);
                rJson.put("time", resultSet.getString("TIME"));
                rJson.put("count", resultSet.getInt("COUNT"));
                //如果为空，则新增 一个类型
                if (rMap.get(srcType) == null) {
                    rMap.put(srcType, new ArrayList<JSONObject>());
                }
                rMap.get(srcType).add(rJson);
            }
        });

        //遍历所有的渠道类型
        for (Integer srcType : rMap.keySet()) {
            JSONObject row = new JSONObject();
            List<JSONObject> srcList = rMap.get(srcType);

            row.put("srcType", srcType);
            //判断null
            if (CollectionUtils.isNotEmpty(srcList)) {
                row.put("srcName", SourceTypeEnum.switchName(srcType));
            }
            //遍历每天的数据，生成 day01-12  这样的天数对应的
            int total = 0;
            for (JSONObject groupList : srcList) {
                //状态表头
                int count = groupList.getIntValue("count");
                row.put("day" + groupList.getString("time"), count);
                total += count;
            }
            row.put("total", total);
            //将这一行加入
            reportsList.add(row);
        }

    }

    /**
     * 封装基础参数
     *
     * @return
     */
    private Map<String, Object> initBaseParamsMap(ReportsParamVO reportsParamVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", reportsParamVO.getCompanyId());
        //这个月第一天
        int start = TimeUtil.getMonthStartTimeStampByDate(reportsParamVO.getMonth());
        //这个月最后一天
        int end = TimeUtil.getMonthEndTimeStampByDate(reportsParamVO.getMonth());
        params.put("start", start);
        params.put("end", end);
        params.put("status", ClientStatusConst.SUCCESS_STATUS_RANGE);
        return params;
    }

    /**
     * 获取where
     *
     * @return
     */
    private String getBaseWhereSql() {
        StringBuilder whereSql = new StringBuilder().append(" WHERE info.COMPANYID = :companyId ")
                .append(" AND  info.ISDEL = 0 ")
                .append(" AND info.SUCCESSTIME BETWEEN  :start AND :end ")
                .append(" AND info.STATUSID in (:status) ");
        return whereSql.toString();
    }

    /**
     * 获取 groupby
     *
     * @return
     */
    private String getBaseGroupBySql() {
        return " GROUP BY info.SRCTYPE, FROM_UNIXTIME( info.SUCCESSTIME, '%Y-%m-%d' ) ";
    }
}
