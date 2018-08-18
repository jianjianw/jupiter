package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.*;
import com.sun.org.apache.regexp.internal.RE;
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
 * @Date: 2018-8-17
 */
@Repository
public class DstgYearsClientDetailReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取年度报表分析
     * */
    public List<DstgYearDetailReportsVO> getDstgYearsClietnDetailReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig)throws SQLException{
        List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS = new ArrayList<>();
        //获取所有年度客资数量
        getAllYearClientCount(reportsParamVO,dstgYearDetailReportsVOS);
        //获取年度无效量
        getInValidClientCount(reportsParamVO,dstgYearDetailReportsVOS,invalidConfig);
        //获取筛选中
        getFilterInClientCount(reportsParamVO,dstgYearDetailReportsVOS);
        //获取筛选待定
        getFilterPendingClientCount(reportsParamVO,dstgYearDetailReportsVOS);
        //获取筛选无效
        getFilterInValidClientCount(reportsParamVO,dstgYearDetailReportsVOS);
        //获取待定
        getPendingClientCount(reportsParamVO,dstgYearDetailReportsVOS,invalidConfig);
        //获取花费
        getClientCost(reportsParamVO,dstgYearDetailReportsVOS);
        //计算数据
        computerRate(reportsParamVO,dstgYearDetailReportsVOS,invalidConfig);

        //计算总计
        computerTotal(reportsParamVO,dstgYearDetailReportsVOS);
        return dstgYearDetailReportsVOS;
    }

    /**
     * 获取客资总量
     * */
    private void getAllYearClientCount(ReportsParamVO reportsParamVO, final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS ){
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        getCommonSqlPartTwo(sb);

        final List<DstgYearDetailReportsVO> dstgYearDetailReports = jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                DstgYearDetailReportsVO dstgYearDetailReportsVO = new DstgYearDetailReportsVO();
                dstgYearDetailReportsVO.setAllClientCount(rs.getInt("client_count"));
                dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                dstgYearDetailReportsVOS.add(dstgYearDetailReportsVO);
                return null;
            }
        });
    }

    /**
     * 获取客资待定量
     * */
    public void getPendingClientCount(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS, DsInvalidVO invalidConfig) throws SQLException{
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        //TODO 添加指定条件
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        getCommonSqlPartTwo(sb);

        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears(),invalidConfig.getDsDdStatus()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setPendingClientCount(rs.getInt("client_count"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });
    }

    /**
     * 获取筛选待定量
     * */
    private void getFilterPendingClientCount(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS)throws SQLException{
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        //TODO 添加指定条件
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 98 ");
        getCommonSqlPartTwo(sb);

        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setFilterPendingClientCount(rs.getInt("client_count"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });

    }

    /**
     * 获取筛选中
     * */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS)throws SQLException{
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        //TODO 添加指定条件
        sb.append(" and info.CLASSID = 1 and info.STATUSID = 0");
        getCommonSqlPartTwo(sb);

        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setFilterInClientCount(rs.getInt("client_count"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });

    }


    /**
     *  获取筛选无效
     * */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS)throws SQLException{
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        //TODO 添加指定条件
        sb.append(" and info.CLASSID = 6 and info.STATUSID = 99");
        getCommonSqlPartTwo(sb);

        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setFilterInValidClientCount(rs.getInt("client_count"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });
    }

    /**
     * 筛选无效量
     * */
    private void getInValidClientCount(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS,DsInvalidVO dsInvalidVO)throws SQLException{
        StringBuilder sb = new StringBuilder();
        getCommonSqlPartOne(reportsParamVO, sb);
        //TODO 添加指定条件
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
        getCommonSqlPartTwo(sb);


        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setInValidClientCount(rs.getInt("client_count"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });
    }


    /**
     * 花费
     * */
    private void getClientCost(ReportsParamVO reportsParamVO,final List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,sum(t.client_cost) AS client_cost ");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(cost.`COSTTIME`)) AS monthNo,  YEAR(FROM_UNIXTIME(cost.`COSTTIME`)) AS myYear,");
        sb.append("  sum(cost.cost) AS client_cost  from hm_crm_source source ");
        sb.append(" left join hm_crm_cost cost on source.id = cost.SRCID  where cost.companyid = ? ");
        sb.append(" group by monthNo");
        sb.append(" ) AS t WHERE t.myYear= ? GROUP BY t.monthNo order by month ");

        jdbcTemplate.query(sb.toString(), new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getYears()}, new RowMapper<DstgYearDetailReportsVO>() {
            @Override
            public DstgYearDetailReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                for (DstgYearDetailReportsVO dstgYearDetailReportsVO:dstgYearDetailReportsVOS){
                    if(dstgYearDetailReportsVO.getMonth().equalsIgnoreCase("month"+rs.getString("month"))){
                        dstgYearDetailReportsVO.setAllCost(rs.getDouble("client_cost"));
                        dstgYearDetailReportsVO.setMonth("month"+rs.getString("month"));
                    }
                }
                return null;
            }
        });
    }


    /**
     *  计算Rate
     */
    private void computerRate(ReportsParamVO reportsParamVO, List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS,DsInvalidVO invalidConfig){
        for (DstgYearDetailReportsVO dstgYearReportsVO:dstgYearDetailReportsVOS){
            //有效量
            if(invalidConfig.getDdIsValid()){
                dstgYearReportsVO.setValidClientCount(dstgYearReportsVO.getAllClientCount()-dstgYearReportsVO.getInValidClientCount()-dstgYearReportsVO.getFilterInClientCount()-dstgYearReportsVO.getFilterInValidClientCount()-dstgYearReportsVO.getFilterPendingClientCount());
            }else{
                dstgYearReportsVO.setValidClientCount(dstgYearReportsVO.getAllClientCount()-dstgYearReportsVO.getPendingClientCount()-dstgYearReportsVO.getInValidClientCount()-dstgYearReportsVO.getFilterInClientCount()-dstgYearReportsVO.getFilterInValidClientCount()-dstgYearReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgYearReportsVO.setClientCount(dstgYearReportsVO.getAllClientCount()-dstgYearReportsVO.getFilterPendingClientCount()-dstgYearReportsVO.getFilterInValidClientCount()-dstgYearReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) dstgYearReportsVO.getValidClientCount() / dstgYearReportsVO.getClientCount();
            dstgYearReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //毛客资成本
            double clientCost = dstgYearReportsVO.getAllCost() / dstgYearReportsVO.getAllClientCount();
            dstgYearReportsVO.setClientCost(parseDouble(((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost) ));
            //有效客资成本
            double validClientCost = dstgYearReportsVO.getClientCost()/dstgYearReportsVO.getValidClientCount();
            dstgYearReportsVO.setValidClientCost(parseDouble(((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost) ));
        }
    }

    /**
     * 计算合计
     * */
    private void computerTotal(ReportsParamVO reportsParamVO, List<DstgYearDetailReportsVO> dstgYearDetailReportsVOS){
        DstgYearDetailReportsVO dstgYearTotal = new DstgYearDetailReportsVO();
        dstgYearTotal.setMonth("总计");
        for (DstgYearDetailReportsVO dstgYearDetailReportsVO : dstgYearDetailReportsVOS) {
            dstgYearTotal.setAllClientCount(dstgYearDetailReportsVO.getAllClientCount() + dstgYearTotal.getAllClientCount());
            dstgYearTotal.setClientCount(dstgYearDetailReportsVO.getClientCount() + dstgYearTotal.getClientCount());
            dstgYearTotal.setValidClientCount(dstgYearDetailReportsVO.getValidClientCount() + dstgYearTotal.getValidClientCount());
            dstgYearTotal.setPendingClientCount(dstgYearDetailReportsVO.getPendingClientCount() + dstgYearTotal.getPendingClientCount());
            dstgYearTotal.setInValidClientCount(dstgYearDetailReportsVO.getInValidClientCount() + dstgYearTotal.getInValidClientCount());
            dstgYearTotal.setClientCost(dstgYearDetailReportsVO.getClientCost() + dstgYearTotal.getClientCost());
         }
        //客资量(总客资-筛选待定-筛选中-筛选无效)
        dstgYearTotal.setClientCount(dstgYearTotal.getAllClientCount()-dstgYearTotal.getFilterPendingClientCount()-dstgYearTotal.getFilterInValidClientCount()-dstgYearTotal.getFilterInClientCount());
        //有效率
        double validRate = (double) dstgYearTotal.getValidClientCount() / dstgYearTotal.getClientCount();
        dstgYearTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //毛客资成本
        double clientCost = dstgYearTotal.getAllCost() / dstgYearTotal.getAllClientCount();
        dstgYearTotal.setClientCost(parseDouble(((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost) ));
        //有效客资成本
        double validClientCost = dstgYearTotal.getClientCost()/dstgYearTotal.getValidClientCount();
        dstgYearTotal.setValidClientCost(parseDouble(((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost) ));

        dstgYearDetailReportsVOS.add(0,dstgYearTotal);
    }



    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }

    private void getCommonSqlPartTwo(StringBuilder sb) {
        sb.append(" group by monthNo");
        sb.append(" ) AS t WHERE t.myYear= ? GROUP BY t.monthNo order by month");
    }

    private void getCommonSqlPartOne(ReportsParamVO reportsParamVO, StringBuilder sb) {
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" SELECT t.myYear AS year,t.monthNo AS month,sum(t.client_count) AS client_count");
        sb.append(" FROM( ");
        sb.append(" SELECT MONTH(FROM_UNIXTIME(info.`CREATETIME`)) AS monthNo,  YEAR(FROM_UNIXTIME(info.`CREATETIME`)) AS myYear, ");
        sb.append("  count(info.id) AS client_count  from "+ infoTabName+" info ");
        sb.append(" left join "+detailTabName+" detail on info.kzid = detail.kzid");
        sb.append(" where info.companyid = ? and info.sourceid is not null");
    }



}
