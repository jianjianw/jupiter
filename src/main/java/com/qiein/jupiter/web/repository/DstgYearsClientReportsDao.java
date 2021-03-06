package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: yyx
 * @Date: 2018-8-16
 */
@Repository
public class DstgYearsClientReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取年度报表分析
     */
    public List<DstgYearReportsVO> getDstgYearsClientReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        List<DstgYearReportsVO> dstgYearReportsVO = new ArrayList<>();
        //获取所有来源
        getAllSource(reportsParamVO, dstgYearReportsVO);
        //获取所有年度客资数量
//        getAllYearClientCount(reportsParamVO, dstgYearReportsVO);
        //computerTotal
//        computerTotal(reportsParamVO,dstgYearReportsVO);
        return dstgYearReportsVO;
    }

    private void getAllSource(ReportsParamVO reportsParamVO, final List<DstgYearReportsVO> dstgYearReportsVO) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id,srcname,SRCIMG from hm_crm_source source where (source.TYPEID = 1 or source.typeid = 2)    and companyid = ?");
        if(StringUtil.isNotEmpty(reportsParamVO.getSourceIds())){
            sb.append(" and id in (" + reportsParamVO.getSourceIds()+")");
        }
        List<DstgYearReportsVO> dstgYearReportsVOS = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()}, new RowMapper<DstgYearReportsVO>() {
            @Override
            public DstgYearReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                DstgYearReportsVO dstgYearReports = new DstgYearReportsVO();
                dstgYearReports.setSourceId(rs.getInt("id"));
                dstgYearReports.setSourceName(rs.getString("srcname"));
                dstgYearReports.setSrcImage(rs.getString("SRCIMG"));
                dstgYearReportsVO.add(dstgYearReports);
                return null;
            }
        });

        DstgYearReportsVO dstgYearReports = new DstgYearReportsVO();
        dstgYearReports.setSourceId(-1);
        dstgYearReports.setSourceName("合计");
        dstgYearReportsVO.add(dstgYearReports);
    }

    private void getAllYearClientCount(ReportsParamVO reportsParamVO, final List<DstgYearReportsVO> dstgYearReportsVO) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,t.sourceid,sum(t.client_count) AS client_count  ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(info.`CREATETIME`)) AS monthNo,  ");
        sb.append(" YEAR(FROM_UNIXTIME(info.`CREATETIME`)) AS myYear, ");
        sb.append("   count(info.id) AS client_count ,info.sourceid ");
        sb.append(" FROM " + infoTabName + " info");
        sb.append(" where info.companyid = ? and info.sourceid is not null");
        if(StringUtil.isNotEmpty(reportsParamVO.getType())){
            sb.append(" and info.typeid in( "+reportsParamVO.getType()+") ");
        }
        if(StringUtil.isNotEmpty(reportsParamVO.getSourceIds())){
            sb.append(" and info.sourceid in (" + reportsParamVO.getSourceIds() +")");
        }
        sb.append(" group by info.sourceid,myYear,monthNo) AS t ");
        sb.append(" WHERE t.myYear= ? ");
        sb.append(" GROUP BY t.monthNo,t.sourceid ");
        sb.append(" order by month ");
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()});
        //获取每月渠道客资数量
        for (DstgYearReportsVO dstgYearReports : dstgYearReportsVO) {
            Map<String, Object> newMap = null;
            if (CollectionUtils.isNotEmpty(dstgYearReports.getMapList())) {
                newMap = dstgYearReports.getMapList();
            } else {
                newMap = new HashMap();
            }
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                String month = String.valueOf(map.get("month").toString());
                if (sourceid.equals(dstgYearReports.getSourceId())) {
                    newMap.put("month" + month, Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
            dstgYearReports.setMapList(newMap);
        }

    }

    /**
     * 计算总计
     */
    public void computerTotal( final List<DstgYearReportsVO> dstgYearReportsVOS) {
        for (DstgYearReportsVO dstgYearReports : dstgYearReportsVOS) {
            Map<String, Object> mapList = dstgYearReports.getMapList();
            Map<String,Object> newsMapList = new HashMap<>();
            for (Map.Entry<String, Object> keys : mapList.entrySet()) {
                Integer kzNum = (Integer)newsMapList.get("合计");
                if(kzNum == null){
                    kzNum = 0;
                }
                newsMapList.put(keys.getKey(),keys.getValue());
                newsMapList.put("合计",  (kzNum+(Integer)keys.getValue()));
            }
            dstgYearReports.setMapList(newsMapList);
        }
    }


}
