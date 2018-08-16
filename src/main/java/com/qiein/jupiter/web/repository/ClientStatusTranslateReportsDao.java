package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.SourceOrderDataReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * （电商客服）客资各个状态转化统计
 *
 * @Author: shiTao
 */
@Repository
public class ClientStatusTranslateReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 获取客资各个状态的转换 统计（小组）
     *
     * @return
     */
    public List<JSONObject> getClientStatusTranslateForGroup(ReportsParamVO reportsParamVO) {
        List<JSONObject> reportsList = new ArrayList<>();

        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", cid);
        params.put("start", reportsParamVO.getStart());
        params.put("end", reportsParamVO.getEnd());

        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.GROUPID,grp.GROUPNAME,info.STATUSID,COUNT( * ) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN hm_pub_group grp ON info.GROUPID = grp.GROUPID ");
        baseSql.append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = :companyId ")
                .append(" AND info.CREATETIME BETWEEN  :start AND :end ");
        //如果组ID 不为空，则
        if (StringUtil.isNotEmpty(reportsParamVO.getGroupIds())) {
            baseSql.append(" AND info.GROUPID in (:groupIds ) ");
            params.put("groupIds", StringUtil.arrStrToList(reportsParamVO.getGroupIds()));
        } else {
            baseSql.append(" AND info.GROUPID IS NOT NULL AND info.GROUPID != '' ");
        }

        baseSql.append(" GROUP BY info.GROUPID,info.STATUSID ");

        //要处理的
        final Map<String, List<JSONObject>> rMap = new HashMap<>();
        namedJdbc.query(baseSql.toString(), params,
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        String groupId = rs.getString("GROUPID");

                        JSONObject rJson = new JSONObject();
                        rJson.put("groupId", groupId);
                        rJson.put("groupName", rs.getString("GROUPNAME"));
                        rJson.put("statusId", rs.getString("STATUSID"));
                        rJson.put("count", rs.getInt("COUNT"));
                        //如果为空，则新增一个组
                        if (rMap.get(groupId) == null) {
                            rMap.put(groupId, new ArrayList<JSONObject>());
                        }
                        rMap.get(groupId).add(rJson);
                    }
                });

        //遍历所有的小组
        for (String groupId : rMap.keySet()) {
            JSONObject row = new JSONObject();
            List<JSONObject> statusList = rMap.get(groupId);
            //判断null
            if (CollectionUtils.isNotEmpty(statusList)) {
                row.put("groupId", groupId);
                row.put("groupName", statusList.get(0).getString("groupName"));
            }
            //遍历一个小组的所有状态，并设置
            for (JSONObject groupList : statusList) {
                //状态表头
                row.put(groupList.getString("statusId"), groupList.getIntValue("count"));
            }
            //将这一行加入
            reportsList.add(row);
        }
        return reportsList;
    }

    /**
     * 获取客资各个状态的转换 统计（小组）
     *
     * @return
     */
    public List<JSONObject> getClientStatusTranslateForGroupStaff(ReportsParamVO reportsParamVO) {
        List<JSONObject> reportsList = new ArrayList<>();

        int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);

        //封装参数
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", cid);
        params.put("start", reportsParamVO.getStart());
        params.put("end", reportsParamVO.getEnd());
        params.put("groupId", reportsParamVO.getGroupId());

        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" SELECT info.APPOINTORID,staff.NICKNAME,info.STATUSID,COUNT( * ) COUNT FROM ");
        baseSql.append(infoTableName).append(" info ")
                .append(" LEFT JOIN hm_pub_staff staff ON staff.ID=info.APPOINTORID ");
        baseSql.append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = :companyId ")
                .append(" AND info.CREATETIME BETWEEN  :start AND :end ")
                .append(" AND info.GROUPID = :groupId ");
        baseSql.append(" GROUP BY info.APPOINTORID,info.STATUSID ");


        //要处理的
        final Map<Integer, List<JSONObject>> rMap = new HashMap<>();

        namedJdbc.query(baseSql.toString(), params,
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        int staffId = rs.getInt("APPOINTORID");

                        JSONObject rJson = new JSONObject();
                        rJson.put("staffId", staffId);
                        rJson.put("nickName", rs.getString("NICKNAME"));
                        rJson.put("statusId", rs.getString("STATUSID"));
                        rJson.put("count", rs.getInt("COUNT"));
                        //如果为空，则新增一个 员工
                        if (rMap.get(staffId) == null) {
                            rMap.put(staffId, new ArrayList<JSONObject>());
                        }
                        rMap.get(staffId).add(rJson);
                    }
                });
        //遍历所有的小组
        for (Integer staffId : rMap.keySet()) {
            JSONObject row = new JSONObject();
            List<JSONObject> statusList = rMap.get(staffId);
            //判断null
            if (CollectionUtils.isNotEmpty(statusList)) {
                row.put("staffId", staffId);
                row.put("nickName", statusList.get(0).getString("nickName"));
            }
            //遍历一个小组的所有状态，并设置
            for (JSONObject groupList : statusList) {
                //状态表头
                row.put(groupList.getString("statusId"), groupList.getIntValue("count"));
            }
            //将这一行加入
            reportsList.add(row);
        }
        return reportsList;
    }


}
