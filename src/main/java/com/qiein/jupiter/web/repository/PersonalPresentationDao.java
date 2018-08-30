package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ReportParamDTO;
import com.qiein.jupiter.web.entity.dto.ZjsClientYearReportDTO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人简报（目前用于钉钉报表)
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/29 15:23
 */
@Repository
public class PersonalPresentationDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    public Map<String,RegionReportsVO> getPersonalPresentation(ReportParamDTO reportParamDTO, DsInvalidVO invalidConfig){
        Map<String,RegionReportsVO> map = new HashMap<>();
        map.put("zjs",getZjsPersonalPresentation(reportParamDTO, invalidConfig).get(0));
        map.put("ds",getDsPersonalPresentation(reportParamDTO, invalidConfig).get(0));
        return map;
    }

    /**
     * 获取个人转介绍简报
     *
     * @param reportParamDTO
     * @param invalidConfig
     * @return
     */
    public List<RegionReportsVO> getZjsPersonalPresentation(ReportParamDTO reportParamDTO, DsInvalidVO invalidConfig) {
        reportParamDTO.setType("zjs");
        String sql = getFinalSQL(reportParamDTO, invalidConfig);
        Map<String, Object> params = null;
        try {
            params = ObjectUtil.getAttributeMap(reportParamDTO);
            params.put("zjsvalidstatus", invalidConfig.getZjsValidStatus());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        final List<RegionReportsVO> total = new ArrayList<>();
        List<RegionReportsVO> total = namedJdbc.query(sql, params,
                new RowMapper<RegionReportsVO>() {
                    @Override
                    public RegionReportsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        RegionReportsVO regionReportsVO = new RegionReportsVO();
                        //总客资
                        regionReportsVO.setAllClientCount(rs.getInt("allClientCount"));
                        //待定量
                        regionReportsVO.setPendingClientCount(rs.getInt("pendingClientCount"));
                        //筛选待定
                        regionReportsVO.setFilterPendingClientCount(rs.getInt("filterPendingClientCount"));
                        //筛选中
                        regionReportsVO.setValidClientCount(rs.getInt("validClientCount"));
                        //有效量
                        regionReportsVO.setFilterInClientCount(rs.getInt("filterInClientCount"));
                        //无效量
                        regionReportsVO.setInValidClientCount(rs.getInt("inValidClientCount"));
                        //筛选无效量
                        regionReportsVO.setFilterInValidClientCount(rs.getInt("filterInValidClientCount"));
                        //入店量
                        regionReportsVO.setComeShopClientCount(rs.getInt("comeShopClientCount"));
                        //成交量
                        regionReportsVO.setSuccessClientCount(rs.getInt("successClientCount"));
                        //成交均价
                        regionReportsVO.setAvgAmount(rs.getInt(("avgAmount")));
                        //营业额
                        regionReportsVO.setAmount(rs.getInt("amount"));
                        return regionReportsVO;
                    }
                }
        );
        //计算各种率
        calculat(total,reportParamDTO.getType(),invalidConfig);
        return total;
    }

    /**
     * 获取电商个人简报
     * @param reportParamDTO
     * @param invalidConfig
     * @return
     */
    public List<RegionReportsVO> getDsPersonalPresentation(ReportParamDTO reportParamDTO, DsInvalidVO invalidConfig) {
        reportParamDTO.setType("ds");
        String sql = getFinalSQL(reportParamDTO, invalidConfig);
        Map<String, Object> params = null;
        try {
            params = ObjectUtil.getAttributeMap(reportParamDTO);
            params.put("zjsvalidstatus", invalidConfig.getZjsValidStatus());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        final List<RegionReportsVO> total = new ArrayList<>();
        List<RegionReportsVO> total = namedJdbc.query(sql, params,
                new RowMapper<RegionReportsVO>() {
                    @Override
                    public RegionReportsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        RegionReportsVO regionReportsVO = new RegionReportsVO();
                        //总客资
                        regionReportsVO.setAllClientCount(rs.getInt("allClientCount"));
                        //待定量
                        regionReportsVO.setPendingClientCount(rs.getInt("pendingClientCount"));
                        //筛选待定
                        regionReportsVO.setFilterPendingClientCount(rs.getInt("filterPendingClientCount"));
                        //筛选中
                        regionReportsVO.setValidClientCount(rs.getInt("validClientCount"));
                        //有效量
                        regionReportsVO.setFilterInClientCount(rs.getInt("filterInClientCount"));
                        //无效量
                        regionReportsVO.setInValidClientCount(rs.getInt("inValidClientCount"));
                        //筛选无效量
                        regionReportsVO.setFilterInValidClientCount(rs.getInt("filterInValidClientCount"));
                        //入店量
                        regionReportsVO.setComeShopClientCount(rs.getInt("comeShopClientCount"));
                        //成交量
                        regionReportsVO.setSuccessClientCount(rs.getInt("successClientCount"));
                        //成交均价
                        regionReportsVO.setAvgAmount(rs.getInt(("avgAmount")));
                        //营业额
                        regionReportsVO.setAmount(rs.getInt("amount"));
                        return regionReportsVO;
                    }
                }
        );
        //计算各种率
        calculat(total,reportParamDTO.getType(),invalidConfig);
        return total;
    }

    /**
     * 生成转介绍最终sql
     *
     * @param reportParamDTO
     * @param invalidConfig
     * @return
     */
    private String getFinalSQL(ReportParamDTO reportParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append("zkz.allClientCount  , ddl.pendingClientCount  , sxdd.filterPendingClientCount  ,yxl.validClientCount  ," +
                        "sxz.filterInClientCount  , wxl.inValidClientCount  , sxwxl.filterInValidClientCount   , " +
                        "rdl.comeShopClientCount   , cjl.successClientCount ,cjjj.avgAmount , yye.amount ")
                .append(" FROM ")
                //总客资
                .append("(" + getAllClientSQL(reportParamDTO.getType()) + ") zkz ,")
                //待定量
                .append("(" + getPendingClientCount(reportParamDTO.getType()) + ") ddl ,")
                //筛选待定
                .append("(" + getFilterPendingClientCount(reportParamDTO.getType()) + ") sxdd ,")
                //筛选中
                .append("(" + getFilterInClientCount(reportParamDTO.getType()) + ") sxz ,")
                //有效量
                .append("(" + getValidClientSQL(reportParamDTO.getType(), invalidConfig) + ")yxl ,")
                //无效量
                .append("(" + getInValidClientSQL(reportParamDTO.getType(), invalidConfig) + ") wxl ,")
                //筛选无效量
                .append("(" + getFilterInValidClientCount(reportParamDTO.getType()) + ") sxwxl ,")
                //入店量
                .append("(" + getComeShopClientSQL(reportParamDTO.getType()) + ") rdl ,")
                //成交量
                .append("(" + getSuccessClientSQL(reportParamDTO.getType()) + ") cjl ,")
                //成交均价
                .append("("+getAvgAmount(reportParamDTO.getType())+") cjjj,")
                //营业额
                .append("("+getAmount(reportParamDTO.getType())+") yye");
        return sb.toString();
    }

    /**
     * 获取基础sql
     *
     * @param sb
     * @return
     */
    private StringBuilder getBaseSQL(StringBuilder sb, String dataName, String type) {
        String subSql = "";
        if (type.equals("zjs")) {
            subSql = " AND src.TYPEID IN (3,4,5) ";
        }
        if (type.equals("ds")) {
            subSql = " AND src.TYPEID IN (1,2) ";
        }
        sb.append("SELECT COUNT(1) " + dataName)
                .append(" FROM hm_crm_client_info info ")
                .append(" INNER JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID ")
                .append(" WHERE info.ISDEL = 0 ")
                .append(" AND info.COMPANYID = :companyid AND info.COLLECTORID = :staffid ")
                .append(subSql);
        return sb;
    }

    /**
     * 获取总客资sql
     *
     * @param type
     * @return
     */
    private String getAllClientSQL(String type) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, "allClientCount", type);
        sb.append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
        return sb.toString();
    }

    /**
     * 获取入店量sql
     *
     * @param type
     * @return
     */
    private String getComeShopClientSQL(String type) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, "comeShopClientCount", type);
        sb.append(" AND (info.COMESHOPTIME BETWEEN :start AND :end ) ");
        return sb.toString();
    }

    /**
     * 获取成交量 successClientCount sql
     *
     * @param type
     */
    private String getSuccessClientSQL(String type) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, "successClientCount", type);
        sb.append(" AND (info.SUCCESSTIME BETWEEN :start AND :end ) ");
        return sb.toString();
    }

    /**
     * 获取有效量 validClientCount sql 有效量 =  总客资-无效-筛选待定-筛选中-筛选无效-待定量?? 有效量应该是只要符合有效指标就算
     *
     * @param type
     */
    private StringBuilder getValidClientSQL(String type, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, "validClientCount", type)
                .append(" AND info.CREATETIME BETWEEN :start AND :end ")
                .append(" AND INSTR( :zjsvalidstatus ,CONCAT( ','+info.STATUSID + '', ','))>0 ");
//        sb.append(" GROUP BY info.SOURCEID");
        return sb;
    }

    /**
     * 获取无效量sql
     *
     * @return
     */
    private String getInValidClientSQL(String type, DsInvalidVO dsInvalidVO) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL, "inValidClientCount", type);
        inValidClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end ) ");
        if ("zjs".equals(type)) {
            if (StringUtil.isNotEmpty(dsInvalidVO.getZjsValidStatus())) {
                inValidClientSQL.append(" AND INSTR('" + dsInvalidVO.getZjsValidStatus() + "',CONCAT( ','+info.STATUSID + '', ','))=0 ");
            }
        }
        if ("ds".equals(type)) {
            if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
                inValidClientSQL.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
                inValidClientSQL.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
            }
            if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
                inValidClientSQL.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ") ");
            }
            if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
                inValidClientSQL.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
            }
        }
//        inValidClientSQL.append("GROUP BY info.SOURCEID");
        return inValidClientSQL.toString();
    }

    /**
     * 获取筛选待定sql
     *
     * @return
     */
    private String getFilterPendingClientCount(String type) {
        StringBuilder filterPendingClientSQL = new StringBuilder();
        getBaseSQL(filterPendingClientSQL, "filterPendingClientCount", type);
        filterPendingClientSQL.append(" AND info.CLASSID = 1 and info.STATUSID = 98 ")
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
//        filterPendingClientSQL.append("GROUP BY info.SOURCEID");
        return filterPendingClientSQL.toString();
    }

    /**
     * 获取筛选中量sql
     */
    private String getFilterInClientCount(String type) {
        StringBuilder filterInClienSQL = new StringBuilder();
        getBaseSQL(filterInClienSQL, "filterInClientCount", type);
        filterInClienSQL.append(" and info.CREATETIME BETWEEN :start AND :end ")
                .append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
//        filterInClienSQL.append("GROUP BY info.SOURCEID");
        return filterInClienSQL.toString();
    }

    /**
     * 获取筛选无效量sql
     *
     * @return
     */
    private String getFilterInValidClientCount(String type) {
        StringBuilder filterInValidClientSQL = new StringBuilder();
        getBaseSQL(filterInValidClientSQL, "filterInValidClientCount", type);
        filterInValidClientSQL.append(" and info.CLASSID = 6 and info.STATUSID = 99 ")
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
//        filterInValidClientSQL.append("GROUP BY info.SOURCEID");
        return filterInValidClientSQL.toString();
    }

    /**
     * 获取待定量sql
     *
     * @return
     */
    private StringBuilder getPendingClientCount(String type) {
        StringBuilder pendingClientSQL = new StringBuilder();
        getBaseSQL(pendingClientSQL, "pendingClientCount", type);
        pendingClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end) ")
                .append(" AND INSTR( :zjsvalidstatus , CONCAT(',',info.STATUSID + '',',')) != 0 ");
//        pendingClientSQL.append(" GROUP BY info.SOURCEID");
        return pendingClientSQL;
    }

    /**
     * 成交均价
     *
     * @param type
     * @return
     */
    private StringBuilder getAvgAmount(String type) {
        StringBuilder avgAmountSQL = new StringBuilder();
        String subSql = "";
        if (type.equals("zjs")) {
            subSql = " AND src.TYPEID IN (3,4,5) ";
        }
        if (type.equals("ds")) {
            subSql = " AND src.TYPEID IN (1,2) ";
        }
        avgAmountSQL.append("SELECT avg(detail.AMOUNT) avgAmount ")
                .append(" FROM hm_crm_client_info info ")
                .append(" INNER JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID ")
                .append(" WHERE info.COMPANYID = :companyid AND info.COLLECTORID = :staffid AND info.ISDEL = 0 ")
                .append(" AND info.SUCCESSTIME BETWEEN :start AND :end ")
                .append(subSql);
        return avgAmountSQL;
    }

    /**
     * 营业额
     *
     * @param type
     * @return
     */
    private String getAmount(String type) {
        StringBuilder amountSQL = new StringBuilder();
        String subSql = "";
        if (type.equals("zjs")) {
            subSql = " AND src.TYPEID IN (3,4,5) ";
        }
        if (type.equals("ds")) {
            subSql = " AND src.TYPEID IN (1,2) ";
        }
        amountSQL.append("SELECT SUM(detail.AMOUNT) amount ")
                .append(" FROM hm_crm_client_info info ")
                .append(" INNER JOIN hm_crm_client_detail detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID ")
                .append(" WHERE info.COMPANYID = :companyid AND info.COLLECTORID = :staffid AND info.ISDEL = 0 ")
                .append(" AND info.SUCCESSTIME BETWEEN :start AND :end ")
                .append(subSql);
        return amountSQL.toString();
    }

    /**
     * 赋值
     * @param total
     */
    private void calculat(List<RegionReportsVO> total, String type, DsInvalidVO invalidConfig) {
        if ("ds".equals(type)){
            for (RegionReportsVO rrv : total) {
                //有效量(总客资-无效量-筛选中-筛选无效-筛选待定)
                if (invalidConfig.getDdIsValid()) {
                    rrv.setValidClientCount(rrv.getAllClientCount() - rrv.getInValidClientCount() - rrv.getFilterInClientCount() - rrv.getFilterInValidClientCount() - rrv.getFilterPendingClientCount());
                } else { // 有效量(总客资-无效量-筛选中-筛选无效-筛选待定-待定量)
                    rrv.setValidClientCount(rrv.getAllClientCount() - rrv.getInValidClientCount() - rrv.getFilterInClientCount() - rrv.getFilterPendingClientCount() - rrv.getFilterInValidClientCount() - rrv.getPendingClientCount());
                }
                //客资量(总客资-筛选待定-筛选中-筛选无效)
                rrv.setClientCount(rrv.getAllClientCount() - rrv.getFilterPendingClientCount() - rrv.getFilterInValidClientCount() - rrv.getFilterInClientCount());
                //计算各种百分比
                everyRate(rrv);

            }
        }if ("zjs".equals(type)){
            for (RegionReportsVO rrv : total) {
                //客资量(总客资-筛选待定-筛选中-筛选无效)
                rrv.setClientCount(rrv.getAllClientCount() - rrv.getFilterPendingClientCount() - rrv.getFilterInValidClientCount() - rrv.getFilterInClientCount());
                //计算各种百分比
                everyRate(rrv);
            }
        }

    }

    /**
     * 计算各种百分比
     *
     * @param rrv
     */
    private void everyRate(RegionReportsVO rrv) {
        //有效率
        double validRate = (double) rrv.getValidClientCount() / rrv.getClientCount();
        rrv.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) rrv.getInValidClientCount() / rrv.getClientCount();
        rrv.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) rrv.getPendingClientCount() / rrv.getClientCount();
        rrv.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) rrv.getComeShopClientCount() / rrv.getClientCount();
        rrv.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) rrv.getComeShopClientCount() / rrv.getValidClientCount();
        rrv.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) rrv.getSuccessClientCount() / rrv.getClientCount();
        rrv.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) rrv.getSuccessClientCount() / rrv.getValidClientCount();
        rrv.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) rrv.getSuccessClientCount() / rrv.getComeShopClientCount();
        rrv.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }
}
