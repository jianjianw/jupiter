package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgSourceYearReportsVO;
import com.qiein.jupiter.web.entity.vo.DstgYearReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-16
 */
@Repository
public class DstgYearsClientReportsRowDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取年度报表分析
     */
    public List<DstgSourceYearReportsVO> getDstgYearsClientReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        List<DstgSourceYearReportsVO> dstgYearReportsVO = new ArrayList<>();
        //获取所有来源
        getAllSource(reportsParamVO, dstgYearReportsVO);
        //获取所有年度客资数量
        getAllYearClientCount(reportsParamVO, dstgYearReportsVO);
        //获取待定
        getPendingClientCount(reportsParamVO,dstgYearReportsVO,invalidConfig);
        //获取无效
        getInValidClientCount(reportsParamVO,dstgYearReportsVO,invalidConfig);
        //筛选待定
        getFilterPending(reportsParamVO,dstgYearReportsVO);
        //筛选中
        getFilterInClientCount(reportsParamVO,dstgYearReportsVO);
        //筛选无效
        getFilterInValidClientCount(reportsParamVO,dstgYearReportsVO);
        //入店量
        getComeShopClientCount(reportsParamVO,dstgYearReportsVO);
        //成交
        getSuccessClientCount(reportsParamVO,dstgYearReportsVO);
        //获取花费
        getAllCost(reportsParamVO,dstgYearReportsVO);
        //获取成交均价
        getAvgAmount(reportsParamVO,dstgYearReportsVO);
        //获取收入
        getSumAmount(reportsParamVO,dstgYearReportsVO);
        //computerTotal
        computerRate(dstgYearReportsVO,invalidConfig);
        computerTotal(reportsParamVO,dstgYearReportsVO);
        return dstgYearReportsVO;
    }

    private void getAllSource(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgYearReportsVO) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id,srcname,SRCIMG from hm_crm_source source where (source.TYPEID = 1 or source.typeid = 2)    and companyid = ?");
        List<DstgYearReportsVO> dstgYearReportsVOS = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId()}, new RowMapper<DstgYearReportsVO>() {
            @Override
            public DstgYearReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (int io = 1; io < 13; io++) {
                    DstgSourceYearReportsVO dstgYearReports = new DstgSourceYearReportsVO();
                    dstgYearReports.setSourceId(rs.getInt("id"));
                    dstgYearReports.setSourceName(rs.getString("srcname"));
                    dstgYearReports.setSourceImage(rs.getString("SRCIMG"));
                    dstgYearReports.setMonth(io);
                    dstgYearReportsVO.add(dstgYearReports);
                }
                DstgSourceYearReportsVO dstgYearReports = new DstgSourceYearReportsVO();
                dstgYearReports.setSourceId(-1);
                dstgYearReports.setSourceName("合计");
                dstgYearReports.setMonth(-1);
                dstgYearReportsVO.add(dstgYearReports);
                return null;
            }
        });
    }

    /**
     * 获取总客资量
     * */
    private void getAllYearClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()});
        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setAllClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取待定量
     * */
    private void getPendingClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS, DsInvalidVO invalidConfig){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()
                ,invalidConfig.getDsDdStatus()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setPendingClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取筛选待定
     * */
    private void getFilterPending(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 98 ");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setFilterPendingClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取筛选中
     * */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 0");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setFilterInClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取筛选无效
     * */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        sb.append(" and info.CLASSID = 6 and info.STATUSID = 99");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setFilterInValidClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }


    /**
     * 获取无效量
     * */
    private void getInValidClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS,DsInvalidVO dsInvalidVO){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and (info.STATUSID in("+ dsInvalidVO.getDsInvalidStatus()+") or");
            sb.append("   detail.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") )");
        }
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())){
            sb.append(" and info.STATUSID in ("+ dsInvalidVO.getDsInvalidStatus()+")");
        }
        if(StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())){
            sb.append(" and detail.YXLEVEL IN("+ dsInvalidVO.getDsInvalidLevel()  +") ");
        }
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setInValidClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取入店量
     * */
    private void getComeShopClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
//        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()
//                ,ClientStatusConst.IS_COME_SHOP
        });

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setComeShopClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }


    /**
     * 获取成交量
     * */
    private void getSuccessClientCount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPart(sb, reportsParamVO);
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        addConditionByTypeAndSourceIds(reportsParamVO, sb);
        getCommonSqlPartB(sb);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears(),ClientStatusConst.IS_SUCCESS});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setSuccessClientCount(Integer.parseInt(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }

    /**
     * 获取花费
     * */
    private void getAllCost(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,t.sourceid,ifnull(sum(t.client_cost),0) AS client_count ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(cost.`COSTTIME`)) AS monthNo,  YEAR(FROM_UNIXTIME(cost.`COSTTIME`)) AS myYear,source.id as sourceid,");
        sb.append("  ifnull(sum(cost.cost),0) AS client_cost  from hm_crm_source source ");
        sb.append(" left join hm_crm_cost cost on source.id = cost.SRCID  where cost.companyid = ? ");
        if (StringUtil.isNotEmpty(reportsParamVO.getSourceIds())) {
            sb.append(" and cost.srcid in (" + reportsParamVO.getSourceIds() + ")");
        }
        sb.append(" group by monthNo,source.id");
        sb.append(" ) AS t WHERE t.myYear= ? GROUP BY t.monthNo,t.sourceid order by month ");
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setAllCost(String.valueOf(map.get("client_count").toString()));
                }
            }
        }

        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
          if(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost())){
              dstgSourceYearReportsVO.setAllCost("0");
          }
        }
    }


    /**
     * 获取成交均价
     * */
    private void getAvgAmount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,t.sourceid,ifnull(sum(t.client_count),0) AS client_count  ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(info.`CREATETIME`)) AS monthNo,  ");
        sb.append(" YEAR(FROM_UNIXTIME(info.`CREATETIME`)) AS myYear, ");
        sb.append("   ifnull(avg(detail.AMOUNT),0) AS client_count ,info.sourceid ");
        sb.append(" FROM " + infoTabName + " info");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid ");
        sb.append(" where info.companyid = ? and info.sourceid is not null and info.isdel = 0");
        addConditionByTypeAndSourceIds(reportsParamVO,sb);
        sb.append(" group by info.sourceid,monthNo) AS t ");
        sb.append(" WHERE t.myYear= ? ");
        sb.append(" GROUP BY t.monthNo,t.sourceid ");
        sb.append(" order by month ");

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setAvgAmount(String.valueOf(map.get("client_count").toString()));
                }
            }
        }
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO:dstgSourceYearReportsVOS){
            if(dstgSourceYearReportsVO.getAvgAmount() == null){
                dstgSourceYearReportsVO.setAvgAmount("0.00");
            }
        }
    }

    /**
     * 获取成交总额
     * */
    private void getSumAmount(ReportsParamVO reportsParamVO, final List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS){
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,t.sourceid,sum(t.client_count) AS client_count  ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(info.`CREATETIME`)) AS monthNo,  ");
        sb.append(" YEAR(FROM_UNIXTIME(info.`CREATETIME`)) AS myYear, ");
        sb.append("  ifnull( sum(detail.AMOUNT),0) AS client_count ,info.sourceid ");
        sb.append(" FROM " + infoTabName + " info");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid ");
        sb.append(" where info.companyid = ? and info.sourceid is not null and info.isdel = 0");
        addConditionByTypeAndSourceIds(reportsParamVO,sb);
        sb.append(" group by info.sourceid,monthNo) AS t ");
        sb.append(" WHERE t.myYear= ? ");
        sb.append(" GROUP BY t.monthNo,t.sourceid ");
        sb.append(" order by month ");
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sb.toString(), new Object[]{reportsParamVO.getCompanyId()
                , reportsParamVO.getYears()});

        //获取每月渠道客资数量
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            for (Map map : mapList) {
                Integer sourceid = Integer.parseInt(String.valueOf(map.get("sourceid").toString()));
                Integer month = Integer.parseInt(String.valueOf(map.get("month").toString()));
                //TODO 每个渠道需要有12个对象
                if (dstgSourceYearReportsVO.getSourceId().equals(sourceid) && month.equals(dstgSourceYearReportsVO.getMonth())) {
                    dstgSourceYearReportsVO.setAmount(Double.parseDouble(String.valueOf(map.get("client_count").toString())));
                }
            }
        }
    }





    private void getCommonSqlPartB(StringBuilder sb) {
        sb.append(" group by info.sourceid,monthNo) AS t ");
        sb.append(" WHERE t.myYear= ? ");
        sb.append(" GROUP BY t.monthNo,t.sourceid ");
        sb.append(" order by month ");
    }

    private void getCommonSqlPart(StringBuilder sb,ReportsParamVO reportsParamVO) {
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,t.sourceid,sum(t.client_count) AS client_count  ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(info.`CREATETIME`)) AS monthNo,  ");
        sb.append(" YEAR(FROM_UNIXTIME(info.`CREATETIME`)) AS myYear, ");
        sb.append("   count(info.id) AS client_count ,info.sourceid ");
        sb.append(" FROM " + infoTabName + " info");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid ");
        sb.append(" where info.companyid = ? and info.sourceid is not null and info.isdel = 0");
    }

    private void addConditionByTypeAndSourceIds(ReportsParamVO reportsParamVO, StringBuilder sb) {
        if (StringUtil.isNotEmpty(reportsParamVO.getType())) {
            sb.append(" and info.typeid in( " + reportsParamVO.getType() + ") ");
        }
        if (StringUtil.isNotEmpty(reportsParamVO.getSourceIds())) {
            sb.append(" and info.sourceid in (" + reportsParamVO.getSourceIds() + ")");
        }
    }

    /**
     * 计算Rate
     * */
    private void computerRate( List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS,DsInvalidVO invalidConfig){
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO:dstgSourceYearReportsVOS){
            //有效量
            if(invalidConfig.getDdIsValid()){
                dstgSourceYearReportsVO.setValidClientCount(dstgSourceYearReportsVO.getAllClientCount()-dstgSourceYearReportsVO.getInValidClientCount()-dstgSourceYearReportsVO.getFilterInClientCount()-dstgSourceYearReportsVO.getFilterInValidClientCount()-dstgSourceYearReportsVO.getFilterPendingClientCount());
            }else{
                dstgSourceYearReportsVO.setValidClientCount(dstgSourceYearReportsVO.getAllClientCount()-dstgSourceYearReportsVO.getPendingClientCount()-dstgSourceYearReportsVO.getInValidClientCount()-dstgSourceYearReportsVO.getFilterInClientCount()-dstgSourceYearReportsVO.getFilterInValidClientCount()-dstgSourceYearReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgSourceYearReportsVO.setClientCount(dstgSourceYearReportsVO.getAllClientCount()-dstgSourceYearReportsVO.getFilterPendingClientCount()-dstgSourceYearReportsVO.getFilterInValidClientCount()-dstgSourceYearReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) dstgSourceYearReportsVO.getValidClientCount() / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) dstgSourceYearReportsVO.getInValidClientCount() / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) dstgSourceYearReportsVO.getPendingClientCount() / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) dstgSourceYearReportsVO.getComeShopClientCount() / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) dstgSourceYearReportsVO.getComeShopClientCount() / dstgSourceYearReportsVO.getValidClientCount();
            dstgSourceYearReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) dstgSourceYearReportsVO.getSuccessClientCount() / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) dstgSourceYearReportsVO.getSuccessClientCount() / dstgSourceYearReportsVO.getValidClientCount();
            dstgSourceYearReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) dstgSourceYearReportsVO.getSuccessClientCount() / dstgSourceYearReportsVO.getComeShopClientCount();
            dstgSourceYearReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
            //毛客资成本
            double clientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost()) ? "0" : dstgSourceYearReportsVO.getAllCost()) / dstgSourceYearReportsVO.getClientCount();
            dstgSourceYearReportsVO.setClientCost(String.valueOf(parseDouble((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost)));
            //有效客资成本
            double validClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost()) ? "0" : dstgSourceYearReportsVO.getAllCost()) / dstgSourceYearReportsVO.getValidClientCount();
            dstgSourceYearReportsVO.setValidClientCost(String.valueOf(parseDouble((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost)));
            //入店成本
            double appointClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost()) ? "0" : dstgSourceYearReportsVO.getAllCost()) / dstgSourceYearReportsVO.getComeShopClientCount();
            dstgSourceYearReportsVO.setComeShopClientCost(String.valueOf(parseDouble((Double.isNaN(appointClientCost) || Double.isInfinite(appointClientCost)) ? 0.0 : appointClientCost)));
            //成交成本
            double successClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost()) ? "0" : dstgSourceYearReportsVO.getAllCost()) / dstgSourceYearReportsVO.getSuccessClientCount();
            dstgSourceYearReportsVO.setSuccessClientCost(String.valueOf(parseDouble((Double.isNaN(successClientCost) || Double.isInfinite(successClientCost)) ? 0.0 : successClientCost)));
            // ROI
            double roi = dstgSourceYearReportsVO.getAmount() / Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost()) ? "0" : dstgSourceYearReportsVO.getAllCost());
            dstgSourceYearReportsVO.setROI(String.valueOf(parseDouble(((Double.isNaN(roi) || Double.isInfinite(roi)) ? 0.0 : roi) * 100)));
        }
    }

    /**
     * 计算总计
     */
    private void computerTotal(ReportsParamVO reportsParamVO, final  List<DstgSourceYearReportsVO> dstgSourceYearReportsVOS) {
        DstgSourceYearReportsVO dstgSourceYearReportsTotal = new DstgSourceYearReportsVO();
        dstgSourceYearReportsTotal.setSourceId(-1);
        dstgSourceYearReportsTotal.setSourceName("合计");
        dstgSourceYearReportsTotal.setMonth(-1);
        for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgSourceYearReportsVOS) {
            dstgSourceYearReportsTotal.setAllClientCount(dstgSourceYearReportsVO.getAllClientCount() + dstgSourceYearReportsTotal.getAllClientCount());
            dstgSourceYearReportsTotal.setClientCount(dstgSourceYearReportsVO.getClientCount() + dstgSourceYearReportsTotal.getClientCount());
            dstgSourceYearReportsTotal.setValidClientCount(dstgSourceYearReportsVO.getValidClientCount() + dstgSourceYearReportsTotal.getValidClientCount());
            dstgSourceYearReportsTotal.setPendingClientCount(dstgSourceYearReportsVO.getPendingClientCount() + dstgSourceYearReportsTotal.getPendingClientCount());
            dstgSourceYearReportsTotal.setInValidClientCount(dstgSourceYearReportsVO.getInValidClientCount() + dstgSourceYearReportsTotal.getInValidClientCount());
            dstgSourceYearReportsTotal.setComeShopClientCount(dstgSourceYearReportsVO.getComeShopClientCount() + dstgSourceYearReportsTotal.getComeShopClientCount());
            dstgSourceYearReportsTotal.setSuccessClientCount(dstgSourceYearReportsVO.getSuccessClientCount() + dstgSourceYearReportsTotal.getSuccessClientCount());
            dstgSourceYearReportsTotal.setAmount(dstgSourceYearReportsVO.getAmount() + dstgSourceYearReportsTotal.getAmount());
            dstgSourceYearReportsTotal.setAvgAmount(String.valueOf(parseDouble(Double.parseDouble(StringUtil.isEmpty(dstgSourceYearReportsVO.getClientCost()) ? "0.00" : dstgSourceYearReportsVO.getClientCost()) + Double.parseDouble(StringUtil.isEmpty(dstgSourceYearReportsTotal.getClientCost()) ? "0.00" : dstgSourceYearReportsTotal.getClientCost()))));
        }

        //客资量(总客资-筛选待定-筛选中-筛选无效)
        dstgSourceYearReportsTotal.setClientCount(dstgSourceYearReportsTotal.getAllClientCount()-dstgSourceYearReportsTotal.getFilterPendingClientCount()-dstgSourceYearReportsTotal.getFilterInValidClientCount()-dstgSourceYearReportsTotal.getFilterInClientCount());
        //有效率
        double validRate = (double) dstgSourceYearReportsTotal.getValidClientCount() / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) dstgSourceYearReportsTotal.getInValidClientCount() / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) dstgSourceYearReportsTotal.getPendingClientCount() / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) dstgSourceYearReportsTotal.getComeShopClientCount() / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) dstgSourceYearReportsTotal.getComeShopClientCount() / dstgSourceYearReportsTotal.getValidClientCount();
        dstgSourceYearReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) dstgSourceYearReportsTotal.getSuccessClientCount() / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) dstgSourceYearReportsTotal.getSuccessClientCount() / dstgSourceYearReportsTotal.getValidClientCount();
        dstgSourceYearReportsTotal.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) dstgSourceYearReportsTotal.getSuccessClientCount() / dstgSourceYearReportsTotal.getComeShopClientCount();
        dstgSourceYearReportsTotal.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
        //毛客资成本
        double clientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsTotal.getAllCost()) ? "0" : dstgSourceYearReportsTotal.getAllCost()) / dstgSourceYearReportsTotal.getClientCount();
        dstgSourceYearReportsTotal.setClientCost(String.valueOf(parseDouble((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost)));
        //有效客资成本
        double validClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsTotal.getAllCost()) ? "0" : dstgSourceYearReportsTotal.getAllCost()) / dstgSourceYearReportsTotal.getValidClientCount();
        dstgSourceYearReportsTotal.setValidClientCost(String.valueOf(parseDouble((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost)));
        //入店成本
        double appointClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsTotal.getAllCost()) ? "0" : dstgSourceYearReportsTotal.getAllCost()) / dstgSourceYearReportsTotal.getComeShopClientCount();
        dstgSourceYearReportsTotal.setComeShopClientCost(String.valueOf(parseDouble((Double.isNaN(appointClientCost) || Double.isInfinite(appointClientCost)) ? 0.0 : appointClientCost)));
        //成交成本
        double successClientCost = Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsTotal.getAllCost()) ? "0" : dstgSourceYearReportsTotal.getAllCost()) / dstgSourceYearReportsTotal.getSuccessClientCount();
        dstgSourceYearReportsTotal.setSuccessClientCost(String.valueOf(parseDouble((Double.isNaN(successClientCost) || Double.isInfinite(successClientCost)) ? 0.0 : successClientCost)));
        // ROI
        double roi = dstgSourceYearReportsTotal.getAmount() / Double.valueOf(StringUtil.isEmpty(dstgSourceYearReportsTotal.getAllCost()) ? "0" : dstgSourceYearReportsTotal.getAllCost());
        dstgSourceYearReportsTotal.setROI(String.valueOf(parseDouble(((Double.isNaN(roi) || Double.isInfinite(roi)) ? 0.0 : roi) * 100)));

        dstgSourceYearReportsVOS.add(0,dstgSourceYearReportsTotal);
    }

    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }


}
