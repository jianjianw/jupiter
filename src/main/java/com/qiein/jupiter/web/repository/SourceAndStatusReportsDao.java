package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.SourceAndStatusReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客资各个渠道各个状态报表
 * author xiangliang
 */
@Repository
public class SourceAndStatusReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 客资各个渠道各个状态
     */
    public List<SourceAndStatusReportsVO> getSourceAndStatusReports(String appointorIds, String collectorIds, String receptorIds, String start, String end, String groupIds, String typeIds, String sourceIds, Integer companyId, List<StatusPO> statusPOS, List<SourcePO> sourcePOS) {
        List<SourceAndStatusReportsVO> list = new ArrayList<>();
        getBaseList(list,statusPOS,sourcePOS);
        StringBuilder sql=new StringBuilder();
        getBaseSql(sql);
        getSqlForSelect(sql,appointorIds,collectorIds,receptorIds,groupIds,typeIds,sourceIds);
        sql.append(" GROUP BY client.STATUSID,client.SOURCEID");
        List<Map<String, Object>> sourceAndStatusReports = jdbcTemplate.queryForList(sql.toString(),
                new Object[]{companyId,start,end});
        //获取数据排列
        for(Map<String,Object> map:sourceAndStatusReports){
            for(SourceAndStatusReportsVO sourceAndStatusReportsVO:list){
                Integer i=Integer.parseInt(Long.toString((Long) (map.get("srcId"))));
                if(Integer.parseInt(Long.toString((Long) (map.get("srcId"))))==sourceAndStatusReportsVO.getSrcId()){
                    sourceAndStatusReportsVO.getMap().put(map.get("status").toString(),Integer.parseInt(Long.toString((Long) (map.get("count")))));
                }
            }
        }
        getCountByStatus(list,statusPOS);
        getCountBySource(list);
        return list;
    }


    /**
     * 获取基础数据排列数组
     * @param list
     * @param statusPOS
     * @param sourcePOS
     */
    private void getBaseList( List<SourceAndStatusReportsVO> list,List<StatusPO> statusPOS, List<SourcePO> sourcePOS){
        for(SourcePO sourcePO:sourcePOS){
            SourceAndStatusReportsVO sourceAndStatusReportsVO=new SourceAndStatusReportsVO();
            sourceAndStatusReportsVO.setSrcId(sourcePO.getId());
            sourceAndStatusReportsVO.setSrcName(sourcePO.getSrcName());
            sourceAndStatusReportsVO.setSrcImg(sourcePO.getSrcImg());
            Map<String,Integer> map=new HashMap<>();
            for(StatusPO statusPO:statusPOS){
                map.put(statusPO.getStatusId()+ CommonConstant.NULL_STR,0);
            }
            sourceAndStatusReportsVO.setMap(map);
            list.add(sourceAndStatusReportsVO);
        }
    }

    /**
     * 获取基础查询语句
     * @param sql
     */
    private void getBaseSql(StringBuilder sql){
        sql.append("SELECT count(1) count,");
        sql.append(" client.SOURCEID srcId,");
        sql.append(" client.STATUSID status,");
        sql.append(" sta.STATUSNAME statusName");
        sql.append(" FROM hm_crm_client_info client");
        sql.append(" LEFT JOIN hm_crm_client_status sta ON client.STATUSID = sta.STATUSID AND client.COMPANYID = sta.COMPANYID");
        sql.append(" WHERE client.COMPANYID = ?");
        sql.append(" AND client.SRCTYPE in (1,2)");
        sql.append(" AND client.CREATETIME BETWEEN ? AND ?");
        sql.append(" and client.isdel =0");
    }

    /**
     * 获取非必要查询条件
     */
    private void getSqlForSelect(StringBuilder sql,String appointorIds, String collectorIds, String receptorIds,String groupIds, String typeIds, String sourceIds){
        if(StringUtil.isNotEmpty(appointorIds)){
            sql.append(" AND client.APPOINTORID in ("+appointorIds+")");
        }
        if(StringUtil.isNotEmpty(collectorIds)){
            sql.append(" AND client.COLLECTORID in ("+collectorIds+")");
        }
        if(StringUtil.isNotEmpty(receptorIds)){
            sql.append(" AND client.RECEPTORID in ("+receptorIds+")");
        }
        if(StringUtil.isNotEmpty(groupIds)){
            sql.append(" AND client.GROUPID in ("+groupIds+")");
        }
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" AND client.TYPEID in ("+typeIds+")");
        }
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append(" AND client.SOURCEID in ("+sourceIds+")");
        }
    }

    /**
     * 计算每个渠道的合计
     */
    private void getCountBySource(List<SourceAndStatusReportsVO> list){
        for(SourceAndStatusReportsVO sourceAndStatusReportsVO:list){
            int hj=0;
            for(String key:sourceAndStatusReportsVO.getMap().keySet()){
                hj+=sourceAndStatusReportsVO.getMap().get(key);
            }
            sourceAndStatusReportsVO.getMap().put("-1",hj);
        }
    }

    /**
     * 计算每个状态的合计
     */
    private void getCountByStatus(List<SourceAndStatusReportsVO> list,List<StatusPO> statusPOS){
        SourceAndStatusReportsVO sourceAndStatusReportsVO1=new SourceAndStatusReportsVO();
        sourceAndStatusReportsVO1.setSrcId(0);
        sourceAndStatusReportsVO1.setSrcName("合计");
        sourceAndStatusReportsVO1.setSrcImg("");
        Map<String,Integer> map=new HashMap<>();
        for(StatusPO statusPO:statusPOS){
            int hj=0;
            for(SourceAndStatusReportsVO sourceAndStatusReportsVO:list){
                hj+=sourceAndStatusReportsVO.getMap().get(statusPO.getStatusId()+"");
            }
            map.put(statusPO.getStatusId()+CommonConstant.NULL_STR,hj);
        }
        sourceAndStatusReportsVO1.setMap(map);
        list.add(0,sourceAndStatusReportsVO1);
    }
}
