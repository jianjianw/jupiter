package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务 月度订单数据统计
 *
 * @Author: shiTao
 */
@Repository
public class CwMonthOrderCountReports {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 获取统计报表
     *
     * @param reportsParamVO
     */
    public void getCwMonthOrderCountReports(ReportsParamVO reportsParamVO) {
        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> stringObjectMap = initBaseParamsMap(reportsParamVO);
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.SRCTYPE,FROM_UNIXTIME( info.SUCCESSTIME, '%d' ) TIME, COUNT( * ) COUNT FROM");
        baseSql.append(infoTableName).append(" info ");
        //拼接where
        baseSql.append(getBaseWhereSql());
        //拼接groupBy
        baseSql.append(getBaseGroupBySql());

        namedJdbc.query(baseSql.toString(), stringObjectMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

            }
        });

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
        return params;
    }

    /**
     * 获取where
     *
     * @return
     */
    private String getBaseWhereSql() {
        String whereSql = " WHERE info.COMPANYID = :companyId " +
                " AND  info.ISDEL = 0 " +
                " AND info.SUCCESSTIME BETWEEN  :start AND :end " +
                " AND info.GROUPID = :groupId ";
        return whereSql;
    }

    /**
     * 获取 groupby
     *
     * @return
     */
    private String getBaseGroupBySql() {
        return " info.SRCTYPE, FROM_UNIXTIME( info.SUCCESSTIME, '%Y-%m-%d' ) ";
    }
}
