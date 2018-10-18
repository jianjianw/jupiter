package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        conditionMap.put("start", vo.getStart());
        conditionMap.put("end", vo.getEnd());

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
                "  AND info.SUCCESSTIME >= info.CREATETIME AND info.SRCTYPE = 1 " +
                " GROUP BY" +
                "  info.SOURCEID," +
                "  cyc";
        final Map<Integer, List<JSONObject>> rMap = new HashMap<>();


        namedJdbc.query(sql, conditionMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int srcId = resultSet.getInt("ID");
                JSONObject rJson = new JSONObject();
                rJson.put("srcName", resultSet.getString("SRCNAME"));
                rJson.put("srcId", resultSet.getInt("ID"));
                rJson.put("cyc", resultSet.getInt("cyc"));
                rJson.put("count", resultSet.getInt("COUNT"));

                //如果为空，则新增 一个类型
                if (rMap.get(srcId) == null) {
                    rMap.put(srcId, new ArrayList<JSONObject>());
                }
                rMap.get(srcId).add(rJson);

            }
        });
        Set<String> stack = new LinkedHashSet<>();
        List<Map<String, Object>> rowsData = new ArrayList<>();
        //  遍历
        for (Integer srcId : rMap.keySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            List<JSONObject> srcList = rMap.get(srcId);
            row.put("渠道", srcList.get(0).getString("srcName"));
            for (JSONObject jsonObject : srcList) {
                int cycDay = jsonObject.getIntValue("cyc");
                if (cycDay == 0) {
                    String desc = "当天";
                    if(!stack.contains(desc)){
                        stack.add(desc);
                    }
                    row.put(desc, jsonObject.getIntValue("count"));
                } else {
                    String desc = cycDay + "天";
                    row.put(desc, jsonObject.getIntValue("count"));
                    if(!stack.contains(desc)){
                        stack.add(desc);
                    }
                }
            }
            rowsData.add(row);
        }
        Set<String> columns = new LinkedHashSet<>();
        columns.add("渠道");
        columns.addAll(stack);

        //前端需要的
        json.put("stack", stack);
        json.put("columns", columns);
        json.put("rows", rowsData);

        return json;
    }
}
