package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.QueryVO;
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
 * 推广渠道订单周期统计
 *
 * @Author: shiTao
 */
@Repository
public class DstgOrderCycleCountDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    public JSONObject getCount(QueryVO vo) {
        JSONObject json = new JSONObject();
        int companyId = vo.getCompanyId();
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("companyId", companyId);

        String sql = "SELECT" +
                "  src.SRCNAME,src.ID," +
                "  ( ( info.SUCCESSTIME - info.CREATETIME ) DIV 86400 ) cyc," +
                "  count( * ) COUNT " +
                " FROM" +
                "  hm_crm_client_info info" +
                "  LEFT JOIN hm_crm_source src ON info.SOURCEID = src.ID" +
                " WHERE " +
                "  info.COMPANYID = :companyId " +
                "  AND info.ISDEL = 0 " +
                "  AND info.CREATETIME BETWEEN :start " +
                "  AND :end " +
                "  AND info.SUCCESSTIME >= info.CREATETIME " +
                " GROUP BY" +
                "  info.SOURCEID," +
                "  cyc";
        final List<JSONObject> list = new ArrayList<>();
        namedJdbc.query(sql, conditionMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JSONObject rJson = new JSONObject();
                rJson.put("srcName", resultSet.getString("SRCNAME"));
                rJson.put("srcId", resultSet.getInt("ID"));
                rJson.put("cyc", resultSet.getInt("cyc"));
                rJson.put("COUNT", resultSet.getInt("COUNT"));
                list.add(rJson);
            }
        });



        return json;
    }
}
