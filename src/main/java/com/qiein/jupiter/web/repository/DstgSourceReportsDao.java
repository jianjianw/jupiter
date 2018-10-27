package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsConditionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 电商推广报表
 * author :xiangliang
 */
@Repository
public class DstgSourceReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<DstgReportsVO> getDstgSourceReports(ReportsConditionVO reportsConditionVO, DsInvalidVO dsInvalidVO)  {

//        List<DstgReportsVO> dstgReportsVOS = getDstgSourceReports(hashMap);
        List<DstgReportsVO> dstgReportsVOS = new ArrayList<>();
        try {
            getCollectorIdLimit(reportsConditionVO);
            //获取渠道与来源
            getChannelAndSource(reportsConditionVO, dstgReportsVOS, dsInvalidVO);
            //获取总客资量
            getAllClientCount(reportsConditionVO, dstgReportsVOS);
            //筛选中
            getScreenWaitClientCount(reportsConditionVO, dstgReportsVOS);
            //筛选待定
            getUndeterWaitClientCount(reportsConditionVO, dstgReportsVOS);
            //筛选无效
            getInvalidWaitClientCount(reportsConditionVO, dstgReportsVOS);
            //待定客资
            getClientWaitCount(reportsConditionVO, dstgReportsVOS, dsInvalidVO);
            //无效客资
            getClientInValidCount(reportsConditionVO, dstgReportsVOS,dsInvalidVO);
            //预约量
            getAppointClientCount(reportsConditionVO, dstgReportsVOS);
            //指定时间预约量
            getAppointClientCountNotContains(reportsConditionVO, dstgReportsVOS);
            getAppointClientCountNotContainsAndCreatetTime(reportsConditionVO, dstgReportsVOS);
            ;
            //入店量
            getComeShopClientCount(reportsConditionVO, dstgReportsVOS);
            //成交量
            getSuccessClientCount(reportsConditionVO, dstgReportsVOS);
            //在线成交量
            getOnLineSuccessClientCount(reportsConditionVO, dstgReportsVOS);
            //到店成交量
            getComeShopSuccessClientCount(reportsConditionVO, dstgReportsVOS);
            //指定时间内成交量
            getSpecifiedTimeSuccessClientCount(reportsConditionVO, dstgReportsVOS);

            //花费
            getSourceCost(reportsConditionVO, dstgReportsVOS);
            //套系总金额
            getClientSuccessAmount(reportsConditionVO, dstgReportsVOS);
            //已收总金额
            getStayClientSuccessAmount(reportsConditionVO, dstgReportsVOS);
            // 成交均价
            getClientAvgSuccessAmount(reportsConditionVO, dstgReportsVOS);
            // 微信数据
            getWechatFlagCount(reportsConditionVO, dstgReportsVOS);

            Boolean flag = dsInvalidVO.getDdIsValid();

            computerData(dstgReportsVOS, flag);

            computerTotal(dstgReportsVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //操作数据库-->
        return dstgReportsVOS;
    }

    /**
     * 推广筛选
     *
     * @
     */
    public void getCollectorIdLimit(ReportsConditionVO reportsConditionVO) {
        if (StringUtils.isEmpty(reportsConditionVO.getTypeLimit())) {
            return;
        }
        if (Integer.parseInt(reportsConditionVO.getTypeLimit()) != 1 && Integer.parseInt(reportsConditionVO.getTypeLimit()) != 2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT rela.STAFFID FROM hm_pub_group_staff rela ");
        sb.append(" INNER JOIN hm_pub_group grp ON grp.COMPANYID = rela.COMPANYID AND grp.GROUPID = rela.GROUPID  ");
        sb.append(" WHERE rela.COMPANYID = ?  AND grp.GROUPTYPE = 'dscj'");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId()});

        List<Integer> staffList = new LinkedList<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            staffList.add(Integer.parseInt(String.valueOf(map.get("STAFFID"))));
        }
        reportsConditionVO.setStaffList(staffList);
    }

    public static void getCollectorLimitSql(StringBuilder sb, ReportsConditionVO reportsConditionVO) {
        List<Integer> staffList = reportsConditionVO.getStaffList();
        if (staffList.size()==0 ) {
            return;
        }
        if(Integer.parseInt(reportsConditionVO.getTypeLimit()) ==1){
            sb.append(" AND client.COLLECTORID IN ( ");
        } else if (Integer.parseInt(reportsConditionVO.getTypeLimit()) == 2){
            sb.append(" AND client.COLLECTORID not IN ( ");
        }
        for (int i = 0; i < staffList.size(); i++) {
            sb.append(staffList.get(i));
            if (i != staffList.size() - 1) {
                sb.append(" , ");
            } else {
                sb.append(" ");
            }
        }
        sb.append(" ) ");
    }


    //获取渠道与来源
    public void getChannelAndSource(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS, DsInvalidVO dsInvalidVO)  {
        
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        List<Map<String, Object>> dstgGoldDataReports;
        if (!dsInvalidVO.isSourceShowStatus()) {
            sb.append(" select id srcId,srcname srcName,channel_priority,channel_typeid,channel_id,PRIORITY,TYPEID from (");
            sb.append("select distinct source.id,source.srcname,channel.PRIORITY as channel_priority,channel.TYPEID as channel_typeid,channel.ID as channel_id,source.PRIORITY,source.TYPEID from (select distinct sourceid  from  " + infoTabName + " client where client.CREATETIME BETWEEN ? AND ? or client.COMESHOPTIME BETWEEN ? AND ? or client.SUCCESSTIME BETWEEN ? AND ? ");
            String sourceid = null;
            if (null != reportsConditionVO.getSourceId()) {
                sourceid = reportsConditionVO.getSourceId();
            }
            if (StringUtils.isNotEmpty(sourceid)) {
                sb.append(" and client.sourceid in (").append(sourceid).append(")");
            }
            getCollectorLimitSql(sb, reportsConditionVO);
            sb.append(" ) client ");
            sb.append(" left join ");
            sb.append(" hm_crm_source source on client.sourceid = source.id");
            sb.append(" left join ");
            sb.append(" hm_crm_channel channel on source.channelId = channel.id");
            sb.append("  where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ? ");
            if (null != reportsConditionVO.getSourceId()) {
                sourceid = reportsConditionVO.getSourceId().toString();
            }
            if (StringUtils.isNotEmpty(sourceid)) {
                sb.append(" and source.id in (").append(sourceid).append(")");
            }
            sb.append(" GROUP BY source.id");
            sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
            sb.append(" ) a");
            sb.append(" union ");
            sb.append(" select id,srcname,channel_priority,channel_typeid,channel_id,PRIORITY,TYPEID from (");
            sb.append(" select source.id,source.srcname,channel.PRIORITY as channel_priority,channel.TYPEID as channel_typeid,channel.ID as channel_id,source.PRIORITY,source.TYPEID from  hm_crm_cost cost");
            sb.append("  left join hm_crm_source source  on cost.SRCID = source.ID ");
            sb.append(" left join hm_crm_channel channel on source.channelId = channel.id ");
            sb.append(" where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ? ");
            if (null != reportsConditionVO.getSourceId()) {
                sourceid = reportsConditionVO.getSourceId();
            }
            
            if (StringUtils.isNotEmpty(sourceid)) {
                sb.append(" and source.id in (").append(sourceid).append(")");
            }
            sb.append("  and  cost.COSTTIME BETWEEN ? AND ? ");
            sb.append(" and (cost.COST !=0 or cost.beforeCost != 0)");
            sb.append(" GROUP BY source.id");
            sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
            sb.append(" ) b");
            sb.append(" order by channel_priority,channel_typeid,channel_id,PRIORITY,TYPEID,srcId ");
           dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                    new Object[]{
                            reportsConditionVO.getStart(),
                            reportsConditionVO.getEnd(),
                            reportsConditionVO.getStart(),
                            reportsConditionVO.getEnd(),
                            reportsConditionVO.getStart(),
                            reportsConditionVO.getEnd(),
                            reportsConditionVO.getCompanyId(),
                            reportsConditionVO.getCompanyId(),
                            reportsConditionVO.getStart(),
                            reportsConditionVO.getEnd()});
        } else {
            sb.append(" select source.id,source.srcname from hm_crm_source source");
            sb.append(" left join ");
            sb.append(" hm_crm_channel channel on source.channelId = channel.id");
            sb.append("  where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ? ");
            String sourceid = null;
            if (null != reportsConditionVO.getSourceId()) {
                sourceid = reportsConditionVO.getSourceId().toString();
            }
            if (StringUtils.isNotEmpty(sourceid)) {
                sb.append(" and source.id in (").append(sourceid).append(")");
            }
            sb.append(" GROUP BY source.id");
            sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
            dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                    new Object[]{reportsConditionVO.getCompanyId()});
        }
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO = new DstgReportsVO();
            //集合中不存在
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSrcName(String.valueOf(map.get("srcName")));
            dstgReportsVOS.add(dstgReportsVO);
        }
       

    }


    //获取总客资量
    public void getAllClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and  client.isdel = 0 and client.companyId = ? AND client.CREATETIME BETWEEN ? AND ? ");
        //TODO 添加条件
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY source.id");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setAllClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setAllClientCount(dstgReportsVO1.getAllClientCount());
                }
            }
        }
    }

    //获取筛选中
    public void getScreenWaitClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();

        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and   client.isdel = 0 and client.companyId = ? and client.CREATETIME BETWEEN ? AND ?  AND client.STATUSID = 0");
        //TODO 添加条件
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setScreenWaitClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setScreenWaitClientCount(dstgReportsVO1.getScreenWaitClientCount());
                }
            }
        }
    }

    //筛选待定
    public void getUndeterWaitClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();

        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" left join hm_pub_company company on company.id = client.companyId");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and  ");
        sb.append(" client.isdel = 0 and client.companyId = ? and client.CREATETIME BETWEEN ? AND ? AND  client.STATUSID = 98 ");
        //TODO 添加条件
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID ");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setUndeterWaitClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setUndeterWaitClientCount(dstgReportsVO1.getUndeterWaitClientCount());
                }
            }
        }
    }

    //筛选无效
    public void getInvalidWaitClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0 and client.companyId = ? and client.CREATETIME BETWEEN ? AND ? AND client.STATUSID = 99");
        //TODO 添加条件
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");

        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setInvalidWaitClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setInvalidWaitClientCount(dstgReportsVO1.getInvalidWaitClientCount());
                }
            }
        }
    }


    //待定客资
    public void getClientWaitCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS, DsInvalidVO dsInvalidVO)  {
        if (StringUtils.isEmpty(dsInvalidVO.getDsDdStatus())) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" left join ");
        sb.append(" hm_pub_company company on company.id = client.COMPANYID");
        sb.append("  where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId = ? AND client.CREATETIME BETWEEN ? AND ? ");
        //这里需要关联公示表进行给查询
//        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        sb.append(" and client.statusid in (" + dsInvalidVO.getDsDdStatus() + ")");
        //TODO 添加条件
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID ");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setWaitClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setWaitClientCount(dstgReportsVO1.getWaitClientCount());
                }
            }
        }
    }



    //无效客资
    public void getClientInValidCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS,DsInvalidVO dsInvalidVO)  {
        if (StringUtils.isEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtils.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" left join");
        sb.append(" " + detailTabName + " detail on client.KZID = detail.KZID");
        sb.append(" left join");
        sb.append(" (select company.id,company.DSINVALIDSTATUS,company.DSINVALIDLEVEL from hm_pub_company company where id = ? ) company");
        sb.append(" on client.COMPANYID = company.id");
        sb.append(" where");
        sb.append(" client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?   and client.CREATETIME BETWEEN ? AND ? and client.isdel = 0");
        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sb.append(" and (client.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            sb.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtils.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sb.append(" and client.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtils.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            sb.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID  ");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setInValidClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setInValidClientCount(dstgReportsVO1.getInValidClientCount());
                }
            }
        }
    }


    //预约量
    public void getAppointClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0   ");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" and client.companyid = ? and client.APPOINTTIME BETWEEN ? AND ?  GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setAppointClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setAppointClientCount(dstgReportsVO1.getAppointClientCount());
                }
            }
        }
    }


    //预约量(只有已预约，不包含待跟踪)
    public void getAppointClientCountNotContains(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0   ");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append("and client.CLASSID = 3 and client.companyid = ? and client.APPOINTTIME BETWEEN ? AND ?  GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setAppointClientCountNotContains(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setAppointClientCountNotContains(dstgReportsVO1.getAppointClientCountNotContains());
                }
            }
        }
    }

    //预约量(今天创建时间 确定意向的客资)
    public void getAppointClientCountNotContainsAndCreatetTime(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  ");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append("and client.CLASSID = 3 and client.companyid = ? and client.CREATETIME BETWEEN ? AND ?  GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setAppointClientCountNotContainsAndCreateTime(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setAppointClientCountNotContainsAndCreateTime(dstgReportsVO1.getAppointClientCountNotContainsAndCreateTime());
                }
            }
        }
    }


    //入店量
    public void getComeShopClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId =? and  client.COMESHOPTIME BETWEEN ? and ? ");
        if (reportsConditionVO.getIsCreate()) {
            sb.append(" and client.createtime between "+reportsConditionVO.getStart()+" and "+reportsConditionVO.getEnd());
        }
//        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        //TODO 条件拼接
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setComeShopClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setComeShopClientCount(dstgReportsVO1.getComeShopClientCount());
                }
            }
        }
    }


    //成交量
    public void getSuccessClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append("  where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId = ?  and client.SUCCESSTIME BETWEEN ? and ? ");
        if (reportsConditionVO.getIsCreate()) {
            sb.append(" and client.createtime between "+reportsConditionVO.getStart()+" and "+reportsConditionVO.getEnd());
        }
        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSuccessClientCount(dstgReportsVO1.getSuccessClientCount());
                }
            }
        }
    }

    //入店成交量
    public void getComeShopSuccessClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append("  where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId = ?  and client.SUCCESSTIME BETWEEN ? and ?  and client.statusid in (9,30)");
        if (reportsConditionVO.getIsCreate()) {
            sb.append(" and client.createtime between "+reportsConditionVO.getStart()+" and "+reportsConditionVO.getEnd());
        }
        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setComeShopSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setComeShopSuccessClientCount(dstgReportsVO1.getComeShopSuccessClientCount());
                }
            }
        }
    }

    //在线成交量
    public void getOnLineSuccessClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append("  where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId = ?  and client.SUCCESSTIME BETWEEN ? and ? and client.statusid=40");
        if (reportsConditionVO.getIsCreate()) {
            sb.append(" and client.createtime between "+reportsConditionVO.getStart()+" and "+reportsConditionVO.getEnd());
        }
        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setOnLineSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setOnLineSuccessClientCount(dstgReportsVO1.getOnLineSuccessClientCount());
                }
            }
        }
    }

    //指定时间内成交量
    public void getSpecifiedTimeSuccessClientCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,count(1) count");
        sb.append(" from " + infoTabName + " client");
        sb.append(" left join ");
        sb.append("hm_crm_source source on client.SOURCEID = source.id  ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append("  where client.SRCTYPE IN ( 1, 2 ) and source.COMPANYID = ?  and    client.isdel = 0  and client.companyId = ? and client.CREATETIME BETWEEN ? and ? and client.SUCCESSTIME BETWEEN ? and ? ");
        sb.append(" AND INSTR( ?, CONCAT(',',client.STATUSID + '',',')) != 0");
        //TODO 条件拼接
        getCollectorLimitSql(sb, reportsConditionVO);
        addCondition(reportsConditionVO, sb);
        sb.append(" GROUP BY SOURCEID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),ClientStatusConst.IS_SUCCESS});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSpecifiedTimeClientCount(Integer.parseInt(String.valueOf(map.get("count"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSpecifiedTimeClientCount(dstgReportsVO1.getSpecifiedTimeClientCount());
                }
            }
        }

    }


    //花费
    public void getSourceCost(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,SUM(cost.cost) amount");
        sb.append(" from hm_crm_source source");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" left join");
        sb.append(" hm_crm_cost cost on cost.srcid = source.id where  (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?  and   cost.companyId = ? AND cost.COSTTIME BETWEEN ? AND ? ");
        //TODO 条件拼接
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and source.id in (").append(sourceid).append(")");
        }
        sb.append(" group by cost.SRCID");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSourceClientCost(String.valueOf(map.get("amount")));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSourceClientCost(dstgReportsVO1.getSourceClientCost());
                }
            }
        }


    }


    //套系总金额
    public void getClientSuccessAmount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,client_success_amount.client_amout amount");
        sb.append(" from ");
        sb.append(" (select client.SOURCEID,SUM(detail.AMOUNT) as client_amout from " + infoTabName + " client," + detailTabName + " detail where client.KZID = detail.KZID and client.isdel = 0 and  client.classId = 5 and client.companyId = ? and client.SUCCESSTIME BETWEEN ? AND ? ");
        String typeid = null;
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        sb.append(" group by client.SOURCEID) as client_success_amount ");
        sb.append(" left join");
        sb.append(" hm_crm_source source on client_success_amount.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        //TODO 条件拼接
        sb.append(" where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?  ");
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and source.id in (").append(sourceid).append(")");
        }
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSuccessClientAmount(Integer.parseInt(String.valueOf(map.get("amount"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSuccessClientAmount(dstgReportsVO1.getSuccessClientAmount());
                }
            }
        }

    }


    //已收总金额
    public void getStayClientSuccessAmount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,client_success_stay_amount.client_stay_amout amount");
        sb.append(" from ");
        sb.append(" (select client.SOURCEID,SUM(detail.STAYAMOUNT) as client_stay_amout from " + infoTabName + " client," + detailTabName + " detail where client.KZID = detail.KZID and client.isdel = 0 and  client.classId = 5  and client.companyId = ? and client.SUCCESSTIME BETWEEN ? AND ? ");
        String typeid = null;
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        sb.append(" group by client.SOURCEID) as client_success_stay_amount ");
        sb.append(" left join");
        sb.append(" hm_crm_source source on client_success_stay_amount.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        //TODO 条件拼接
        sb.append(" where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?  ");
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and source.id in (").append(sourceid).append(")");
        }
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSuccessClientStayAmount(Integer.parseInt(String.valueOf(map.get("amount"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSuccessClientStayAmount(dstgReportsVO1.getSuccessClientStayAmount());
                }
            }
        }

    }


    // 成交均价
    public void getClientAvgSuccessAmount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,client_avg_success_amount.client_amout amount");
        sb.append(" from ");
        sb.append(" (select client.SOURCEID,AVG(detail.AMOUNT) as client_amout from " + infoTabName + " client," + detailTabName + " detail  ");
        sb.append(" where client.KZID = detail.KZID and client.isdel = 0 and  client.classId = 5 and client.companyId = ? and client.SUCCESSTIME BETWEEN ? AND ?");
        String typeid = null;
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        sb.append(" group by client.SOURCEID) as client_avg_success_amount ");
        sb.append(" left join");
        sb.append(" hm_crm_source source on client_avg_success_amount.SOURCEID = source.id");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        //TODO 条件拼接
        sb.append(" where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?  ");
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and source.id in (").append(sourceid).append(")");
        }

        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setClientAvgAmount(String.valueOf(map.get("amount")));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setClientAvgAmount(dstgReportsVO1.getClientAvgAmount());
                }
            }
        }

    }


    // 微信数据
    public void getWechatFlagCount(ReportsConditionVO reportsConditionVO, List<DstgReportsVO> dstgReportsVOS)  {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsConditionVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsConditionVO.getCompanyId());
        sb.append(" select source.id srcId,source.srcname,ifnull(success_wechat_count.success_wechat_count,0) scount,ifnull(wait_wechat_count.wait_wechat_count,0) wcount,ifnull(fail_wechat_count.fail_wechat_count,0) fcount");
        sb.append(" from ");
        sb.append(" hm_crm_source source ");
        sb.append(" left join ");
        sb.append(" hm_crm_channel channel on source.channelId = channel.id");
        sb.append(" left join");
        sb.append("(select count(WEFLAG) success_wechat_count,SOURCEID  from " + infoTabName + " client where weflag = 1 AND client.isdel = 0 and companyid = ? and client.CREATETIME BETWEEN ? AND ?");
        String typeid = null;
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and client.sourceid in (").append(sourceid).append(")");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        sb.append(" group by sourceid) success_wechat_count on success_wechat_count.sourceid = source.id");

        sb.append(" left join");
        sb.append("(select count(WEFLAG) wait_wechat_count,SOURCEID  from " + infoTabName + " client where weflag = 0 AND client.isdel = 0 and companyid = ? and client.CREATETIME BETWEEN ? AND ?");
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and client.sourceid in (").append(sourceid).append(")");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        sb.append(" group by sourceid) wait_wechat_count on wait_wechat_count.sourceid = source.id");

        sb.append(" left join");
        sb.append("(select count(WEFLAG) fail_wechat_count,SOURCEID  from " + infoTabName + " client where weflag = 2 AND client.isdel = 0 and companyid = ? and client.CREATETIME BETWEEN ? AND ?");
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId().toString();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and client.sourceid in (").append(sourceid).append(")");
        }
        getCollectorLimitSql(sb, reportsConditionVO);
        sb.append(" group by sourceid) fail_wechat_count on fail_wechat_count.sourceid = source.id");
        sb.append(" where (source.TYPEID = 1 or source.TYPEID = 2) and source.COMPANYID = ?  ");
        sb.append(" ORDER BY channel.PRIORITY,channel.TYPEID,channel.ID,source.PRIORITY,source.TYPEID,source.ID");

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId(),reportsConditionVO.getStart(),reportsConditionVO.getEnd(),reportsConditionVO.getCompanyId()});
        List<DstgReportsVO> dstgReportsVOSbak=new ArrayList<>();
        for(Map<String,Object> map:dstgGoldDataReports){
            DstgReportsVO dstgReportsVO=new DstgReportsVO();
            dstgReportsVO.setId(Integer.parseInt(String.valueOf(map.get("srcId"))));
            dstgReportsVO.setSuccessWeChat(Integer.parseInt(String.valueOf(map.get("scount"))));
            dstgReportsVO.setWaitWeChat(Integer.parseInt(String.valueOf(map.get("wcount"))));
            dstgReportsVO.setFailWeChat(Integer.parseInt(String.valueOf(map.get("fcount"))));
        }
        for(DstgReportsVO dstgReportsVO:dstgReportsVOS){
            for(DstgReportsVO dstgReportsVO1:dstgReportsVOSbak){
                if(dstgReportsVO.getId()==dstgReportsVO1.getId()){
                    dstgReportsVO.setSuccessWeChat(dstgReportsVO1.getSuccessWeChat());
                    dstgReportsVO.setWaitWeChat(dstgReportsVO1.getWaitWeChat());
                    dstgReportsVO.setFailWeChat(dstgReportsVO1.getFailWeChat());
                }
            }
        }

    }


    //计算数据
    public void computerData(List<DstgReportsVO> dstgReportsVOS, Boolean flag) {
        for (DstgReportsVO dstgReportsVO : dstgReportsVOS) {
            //有效客资(总客资-筛选客资-无效客资-筛选中-筛选待定-筛选无效)
            if (flag) {
                dstgReportsVO.setValidClientCount(dstgReportsVO.getAllClientCount() - dstgReportsVO.getInValidClientCount() - dstgReportsVO.getScreenWaitClientCount() - dstgReportsVO.getInvalidWaitClientCount() - dstgReportsVO.getUndeterWaitClientCount());
            } else {
                dstgReportsVO.setValidClientCount(dstgReportsVO.getAllClientCount() - dstgReportsVO.getWaitClientCount() - dstgReportsVO.getInValidClientCount() - dstgReportsVO.getScreenWaitClientCount() - dstgReportsVO.getInvalidWaitClientCount() - dstgReportsVO.getUndeterWaitClientCount());
            }
            //客资量
            dstgReportsVO.setUnAllClientCount(dstgReportsVO.getAllClientCount() - dstgReportsVO.getScreenWaitClientCount() - dstgReportsVO.getInvalidWaitClientCount() - dstgReportsVO.getUndeterWaitClientCount());
            //有效率
            double validRate = (double) dstgReportsVO.getValidClientCount() / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));

            //无效率
            double invalidRate = (double) dstgReportsVO.getInValidClientCount() / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));

            //筛选率
            double waitRate = (double) dstgReportsVO.getWaitClientCount() / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));

            //毛客资入店率
            double appointRate = (double) dstgReportsVO.getComeShopClientCount() / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setAppointRate(parseDouble(((Double.isNaN(appointRate) || Double.isInfinite(appointRate)) ? 0.0 : appointRate) * 100));

            //有效客资入店量
            double validAppointRate = (double) dstgReportsVO.getComeShopClientCount() / dstgReportsVO.getValidClientCount();
            dstgReportsVO.setValidAppointRate(parseDouble(((Double.isNaN(validAppointRate) || Double.isInfinite(validAppointRate)) ? 0.0 : validAppointRate) * 100));

            //毛客资成交率
            double successRate = (double) dstgReportsVO.getSuccessClientCount() / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));

            //有效客资成交率
            double validSuccessRate = (double) dstgReportsVO.getSuccessClientCount() / dstgReportsVO.getValidClientCount();
            dstgReportsVO.setValidSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));

            //入店成交率
            double comeShopSuccessRate = (double) dstgReportsVO.getSuccessClientCount() / dstgReportsVO.getComeShopClientCount();
            dstgReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

            //毛客资成本
            double clientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost()) / dstgReportsVO.getUnAllClientCount();
            dstgReportsVO.setClientCost(String.valueOf(parseDouble((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost)));

            //有效客资成本
            double validClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost()) / dstgReportsVO.getValidClientCount();
            dstgReportsVO.setValidClientCost(String.valueOf(parseDouble((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost)));

            //入店成本
            double appointClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost()) / dstgReportsVO.getComeShopClientCount();
            dstgReportsVO.setAppointClientCost(String.valueOf(parseDouble((Double.isNaN(appointClientCost) || Double.isInfinite(appointClientCost)) ? 0.0 : appointClientCost)));

            //成交成本
            double successClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost()) / dstgReportsVO.getSuccessClientCount();
            dstgReportsVO.setSuccessClientCost(String.valueOf(parseDouble((Double.isNaN(successClientCost) || Double.isInfinite(successClientCost)) ? 0.0 : successClientCost)));

            //总已收占比
            double receiveRatio = (double) dstgReportsVO.getSuccessClientStayAmount() / dstgReportsVO.getSuccessClientAmount();
            dstgReportsVO.setReceiveRatio(parseDouble(((Double.isNaN(receiveRatio) || Double.isInfinite(receiveRatio)) ? 0.0 : receiveRatio) * 100));
            //roi
            double roi = dstgReportsVO.getSuccessClientAmount() / Double.valueOf(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost());
            dstgReportsVO.setRoi(String.valueOf(parseDouble(((Double.isNaN(roi) || Double.isInfinite(roi)) ? 0.0 : roi))));

            //成交均价
            double clientAvgAmount = (double) dstgReportsVO.getSuccessClientAmount() / dstgReportsVO.getSuccessClientCount();
            dstgReportsVO.setClientAvgAmount(String.valueOf(parseDouble((Double.isNaN(clientAvgAmount) || Double.isInfinite(clientAvgAmount)) ? 0.0 : clientAvgAmount)));
        }
        for (DstgReportsVO dstgReportsVO : dstgReportsVOS) {
            dstgReportsVO.setSourceClientCost(String.valueOf(parseDouble(Double.parseDouble(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0" : dstgReportsVO.getSourceClientCost()))));
        }
    }

    //计算总计
    public void computerTotal(List<DstgReportsVO> dstgReportsVOS) {
        DstgReportsVO dstgReportsTotal = new DstgReportsVO();
        dstgReportsTotal.setSrcName("合计");
        for (DstgReportsVO dstgReportsVO : dstgReportsVOS) {
            dstgReportsTotal.setAllClientCount(dstgReportsVO.getAllClientCount() + dstgReportsTotal.getAllClientCount());
            dstgReportsTotal.setUnAllClientCount(dstgReportsVO.getUnAllClientCount() + dstgReportsTotal.getUnAllClientCount());
            dstgReportsTotal.setValidClientCount(dstgReportsVO.getValidClientCount() + dstgReportsTotal.getValidClientCount());
            dstgReportsTotal.setWaitClientCount(dstgReportsVO.getWaitClientCount() + dstgReportsTotal.getWaitClientCount());
            dstgReportsTotal.setInValidClientCount(dstgReportsVO.getInValidClientCount() + dstgReportsTotal.getInValidClientCount());
            dstgReportsTotal.setAppointClientCount(dstgReportsVO.getAppointClientCount() + dstgReportsTotal.getAppointClientCount());
            dstgReportsTotal.setComeShopClientCount(dstgReportsVO.getComeShopClientCount() + dstgReportsTotal.getComeShopClientCount());
            dstgReportsTotal.setSuccessClientCount(dstgReportsVO.getSuccessClientCount() + dstgReportsTotal.getSuccessClientCount());
            dstgReportsTotal.setSpecifiedTimeClientCount(dstgReportsVO.getSpecifiedTimeClientCount() + dstgReportsTotal.getSpecifiedTimeClientCount());
            dstgReportsTotal.setAppointClientCountNotContains(dstgReportsVO.getAppointClientCountNotContains() + dstgReportsTotal.getAppointClientCountNotContains());
            dstgReportsTotal.setAppointClientCountNotContainsAndCreateTime(dstgReportsVO.getAppointClientCountNotContainsAndCreateTime() + dstgReportsTotal.getAppointClientCountNotContainsAndCreateTime());
            dstgReportsTotal.setSuccessWeChat(dstgReportsVO.getSuccessWeChat() + dstgReportsTotal.getSuccessWeChat());
            dstgReportsTotal.setFailWeChat(dstgReportsVO.getFailWeChat() + dstgReportsTotal.getFailWeChat());
            dstgReportsTotal.setWaitWeChat(dstgReportsVO.getWaitWeChat() + dstgReportsTotal.getWaitWeChat());
            dstgReportsTotal.setSuccessClientAmount(dstgReportsVO.getSuccessClientAmount() + dstgReportsTotal.getSuccessClientAmount());
            dstgReportsTotal.setSuccessClientStayAmount(dstgReportsVO.getSuccessClientStayAmount() + dstgReportsTotal.getSuccessClientStayAmount());
            dstgReportsTotal.setOnLineSuccessClientCount(dstgReportsVO.getOnLineSuccessClientCount() + dstgReportsTotal.getOnLineSuccessClientCount());
            dstgReportsTotal.setComeShopSuccessClientCount(dstgReportsVO.getComeShopSuccessClientCount() + dstgReportsTotal.getComeShopSuccessClientCount());
            dstgReportsTotal.setSourceClientCost(String.valueOf(parseDouble(Double.parseDouble(StringUtils.isEmpty(dstgReportsVO.getSourceClientCost()) ? "0.00" : dstgReportsVO.getSourceClientCost()) + Double.parseDouble(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0.00" : dstgReportsTotal.getSourceClientCost()))));
        }
        //有效率
        double validRate = (double) dstgReportsTotal.getValidClientCount() / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));

        //无效率
        double invalidRate = (double) dstgReportsTotal.getInValidClientCount() / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));

        //筛选率
        double waitRate = (double) dstgReportsTotal.getWaitClientCount() / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));

        //毛客资入店率
        double appointRate = (double) dstgReportsTotal.getComeShopClientCount() / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setAppointRate(parseDouble(((Double.isNaN(appointRate) || Double.isInfinite(appointRate)) ? 0.0 : appointRate) * 100));

        //有效客资入店量
        double validAppointRate = (double) dstgReportsTotal.getComeShopClientCount() / dstgReportsTotal.getValidClientCount();
        dstgReportsTotal.setValidAppointRate(parseDouble(((Double.isNaN(validAppointRate) || Double.isInfinite(validAppointRate)) ? 0.0 : validAppointRate) * 100));

        //毛客资成交率
        double successRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));

        //有效客资成交率
        double validSuccessRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getValidClientCount();
        dstgReportsTotal.setValidSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));

        //入店成交率
        double comeShopSuccessRate = (double) dstgReportsTotal.getSuccessClientCount() / dstgReportsTotal.getComeShopClientCount();
        dstgReportsTotal.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

        //毛客资成本
        double clientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0" : dstgReportsTotal.getSourceClientCost()) / dstgReportsTotal.getUnAllClientCount();
        dstgReportsTotal.setClientCost(String.valueOf(parseDouble((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost)));

        //有效客资成本
        double validClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0" : dstgReportsTotal.getSourceClientCost()) / dstgReportsTotal.getValidClientCount();
        dstgReportsTotal.setValidClientCost(String.valueOf(parseDouble((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost)));

        //入店成本
        double appointClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0" : dstgReportsTotal.getSourceClientCost()) / dstgReportsTotal.getComeShopClientCount();
        dstgReportsTotal.setAppointClientCost(String.valueOf(parseDouble((Double.isNaN(appointClientCost) || Double.isInfinite(appointClientCost)) ? 0.0 : appointClientCost)));

        //成交成本
        double successClientCost = Double.valueOf(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0" : dstgReportsTotal.getSourceClientCost()) / dstgReportsTotal.getSuccessClientCount();
        dstgReportsTotal.setSuccessClientCost(String.valueOf(parseDouble((Double.isNaN(successClientCost) || Double.isInfinite(successClientCost)) ? 0.0 : successClientCost)));

        //总已收占比
        double receiveRatio = (double) dstgReportsTotal.getSuccessClientStayAmount() / dstgReportsTotal.getSuccessClientAmount();
        dstgReportsTotal.setReceiveRatio(parseDouble(((Double.isNaN(receiveRatio) || Double.isInfinite(receiveRatio)) ? 0.0 : receiveRatio) * 100));
        //roi
        double roi = dstgReportsTotal.getSuccessClientAmount() / Double.valueOf(StringUtils.isEmpty(dstgReportsTotal.getSourceClientCost()) ? "0.00" : dstgReportsTotal.getSourceClientCost());
        dstgReportsTotal.setRoi(String.valueOf(parseDouble(((Double.isNaN(roi) || Double.isInfinite(roi)) ? 0.0 : roi))));

        //成交均价
        double clientAvgAmount = (double) dstgReportsTotal.getSuccessClientAmount() / dstgReportsTotal.getSuccessClientCount();
        dstgReportsTotal.setClientAvgAmount(String.valueOf(parseDouble((Double.isNaN(clientAvgAmount) || Double.isInfinite(clientAvgAmount)) ? 0.0 : clientAvgAmount)));


        dstgReportsVOS.add(0, dstgReportsTotal);
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }



    //添加条件
    private void addCondition(ReportsConditionVO reportsConditionVO, StringBuilder sb) {

        String typeid = null;
        if (null != reportsConditionVO.getTypeId()) {
            typeid = reportsConditionVO.getTypeId();
        }
        if (StringUtils.isNotEmpty(typeid)) {
            sb.append(" and client.typeid in (").append(typeid).append(")");
        }
        String sourceid = null;
        if (null != reportsConditionVO.getSourceId()) {
            sourceid = reportsConditionVO.getSourceId();
        }
        if (StringUtils.isNotEmpty(sourceid)) {
            sb.append(" and source.id in (").append(sourceid).append(")");
        }


    }
}
