package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 无效原因报表
 * author xiangliang
 */
@Repository
public class InvalidReasonReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 报表显示
     * @param list
     * @param tableInfo
     * @param tableDetail
     * @param companyId
     * @param sourceIds
     * @param startTime
     * @param endTime
     * @param typeIds
     * @return
     */
    public List<Map<String, Object>> getInvalidReasonReports(List<DictionaryPO> list, String tableInfo, String tableDetail, Integer companyId, String sourceIds, String startTime, String endTime, String typeIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT src.SRCNAME,src.SRCIMG");
        sql.append(",(SELECT count(detail.ID) FROM " + tableDetail + " detail LEFT JOIN " + tableInfo + " info ON info.KZID = detail.KZID WHERE src.ID = info.SOURCEID");
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" AND info.TYPEID IN ("+typeIds+")");
        }
        if(StringUtil.isNotEmpty(startTime)&&StringUtil.isNotEmpty(endTime)){
            sql.append(" AND info.CREATETIME > "+startTime);
            sql.append(" AND info.CREATETIME < "+endTime);
        }
        sql.append(" AND (");
        sql.append("detail.INVALIDLABEL='" + list.get(0).getDicName() + "'");
        for (int i = 1; i < list.size(); i++) {
            sql.append("OR detail.INVALIDLABEL='" + list.get(i).getDicName() + "'");
        }
        sql.append(")) hj");
        for (DictionaryPO dictionaryPO : list) {
            sql.append(",(SELECT count(detail.ID) FROM " + tableDetail + " detail LEFT JOIN " + tableInfo + " info ON info.KZID = detail.KZID WHERE src.ID = info.SOURCEID ");
            if(StringUtil.isNotEmpty(typeIds)){
                sql.append(" AND info.TYPEID IN ("+typeIds+")");
            }
            if(StringUtil.isNotEmpty(startTime)&&StringUtil.isNotEmpty(endTime)){
                sql.append(" AND info.CREATETIME > "+startTime);
                sql.append(" AND info.CREATETIME < "+endTime);
            }
            sql.append(" AND detail.INVALIDLABEL ='" + dictionaryPO.getDicName() + "') " + dictionaryPO.getDicType());
        }
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND src.TYPEID=1");
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append("AND src.ID IN ("+sourceIds+")");
        }
        System.out.println(sql.toString());
        List<Map<String, Object>> invalidalbel = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId});
        StringBuilder hjsql = new StringBuilder();
        hjsql.append("SELECT '合计' SRCNAME,'' SRCIMG");
        hjsql.append(",(SELECT count(detail.ID) FROM " + tableDetail + " detail LEFT JOIN " + tableInfo + " info ON detail.KZID=info.KZID " );
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" AND info.TYPEID IN ("+typeIds+")");
        }
        if(StringUtil.isNotEmpty(startTime)&&StringUtil.isNotEmpty(endTime)){
            sql.append(" AND info.CREATETIME > "+startTime);
            sql.append(" AND info.CREATETIME < "+endTime);
        }
        hjsql.append( " where info.SOURCEID IN (select ID from hm_crm_source where COMPANYID=? and TYPEID IN (1,2)");
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append(" AND src.ID IN ("+sourceIds+")");
        }
        hjsql.append(") AND(");
        hjsql.append("detail.INVALIDLABEL='" + list.get(0).getDicName() + "'");
        for (int i = 1; i < list.size(); i++) {
            hjsql.append("OR detail.INVALIDLABEL='" + list.get(i).getDicName() + "'");
        }
        hjsql.append(")) hj");
        for (DictionaryPO dictionaryPO : list) {
            hjsql.append(",(SELECT count(detail.ID) FROM " + tableDetail + " detail LEFT JOIN " + tableInfo + " info ON detail.KZID=info.KZID " );
            if(StringUtil.isNotEmpty(typeIds)){
                sql.append(" AND info.TYPEID IN ("+typeIds+")");
            }
            if(StringUtil.isNotEmpty(startTime)&&StringUtil.isNotEmpty(endTime)){
                sql.append(" AND info.CREATETIME > "+startTime);
                sql.append(" AND info.CREATETIME < "+endTime);
            }
            hjsql.append(" WHERE info.SOURCEID IN (select ID from hm_crm_source where COMPANYID="+companyId+" and TYPEID IN (1,2)");
            if(StringUtil.isNotEmpty(sourceIds)){
                sql.append(" AND src.ID IN ("+sourceIds+")");
            }
            hjsql.append(") AND detail.INVALIDLABEL ='" + dictionaryPO.getDicName() + "') " + dictionaryPO.getDicType());
        }
        System.out.println(hjsql.toString());
        List<Map<String, Object>> hjInvalidalbel = jdbcTemplate.queryForList(hjsql.toString(), new Object[]{companyId});
        hjInvalidalbel.addAll(invalidalbel);
        return hjInvalidalbel;
    }
}
