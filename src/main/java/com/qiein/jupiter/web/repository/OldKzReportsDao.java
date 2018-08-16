package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 老客信息汇总报表
 * author xiangliang
 */
@Repository
public class OldKzReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OldKzReportsVO> getReports(Integer companyId, String startTime, String endTime, String kzNameOrPhone, DsInvalidVO dsInvalidVO) {
        String tableInfo = DBSplitUtil.getTable(TableEnum.info, companyId);
        String tableDetail = DBSplitUtil.getTable(TableEnum.detail, companyId);
        List<OldKzReportsVO> oldKzReportsVOS = new ArrayList<>();
        getAllClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getPendingClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail, dsInvalidVO);
        getComeShopClient(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getSuccessClient(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getFilterWaitClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getFilterInValidClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getFilterInValidClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getInValidClientCount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail, dsInvalidVO);
        getAvgAmount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        getAmount(startTime, endTime, oldKzReportsVOS, kzNameOrPhone, tableInfo, tableDetail);
        computerRate(oldKzReportsVOS, dsInvalidVO);
        computerTotal(oldKzReportsVOS);
        return oldKzReportsVOS;
    }

    /**
     * 总客资
     */
    private void getAllClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsVOS.add(oldKzReportsVO);
        }
    }

    /**
     * 待定量
     */
    private void getPendingClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setPendingClientCount(oldKzReportsVO1.getPendingClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setPendingClientCount(oldKzReportsVO1.getPendingClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 入店量
     */
    private void getComeShopClient(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.ComeShopTime between ? and ?");
       sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setComeShopClientCount(oldKzReportsVO1.getComeShopClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setComeShopClientCount(oldKzReportsVO1.getComeShopClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 成交量
     */
    private void getSuccessClient(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setSuccessClientCount(oldKzReportsVO1.getSuccessClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setSuccessClientCount(oldKzReportsVO1.getSuccessClientCount());
                        break;
                    }
                }

            }
        }

    }

    /**
     * 筛选待定
     */
    private void getFilterWaitClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setFilterPendingClientCount(oldKzReportsVO1.getFilterPendingClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setFilterPendingClientCount(oldKzReportsVO1.getFilterPendingClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setFilterInValidClientCount(oldKzReportsVO1.getFilterInValidClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setFilterInValidClientCount(oldKzReportsVO1.getFilterInValidClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        sql.append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setFilterInClientCount(oldKzReportsVO1.getFilterInClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setFilterInClientCount(oldKzReportsVO1.getFilterInClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 无效
     */
    private void getInValidClientCount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo, tableDetail);
        sql.append(" and info.createtime between ? and ?");
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sql.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            sql.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            sql.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            sql.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
       sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname  ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setInValidClientCount(oldKzReportsVO1.getInValidClientCount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setInValidClientCount(oldKzReportsVO1.getInValidClientCount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 成交均价
     */
    private void getAvgAmount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT detail.OLDKZNAME oldKzName,detail.OLDKZPHONE oldKzPhone, AVG( detail.AMOUNT) avg_amount ");
        sql.append("FROM ");
        sql.append(tableInfo + "  info ");
        sql.append("LEFT JOIN " + tableDetail + " detail ON info.KZID = detail.KZID ");
        sql.append("where (detail.OLDKZNAME  is not null  or detail.OLDKZPHONE is not null)");
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" and (detail.oldKzName like concat('%',?,'%') or detail.oldKzPhone like concat('%',?,'%'))");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{ kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setAvgAmount(((BigDecimal) map.get("avg_amount")).doubleValue());
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone()) && oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())) {
                    oldKzReportsVO.setAvgAmount(oldKzReportsVO1.getAvgAmount());
                    break;
                }
            }
        }
    }

    /**
     * 成交总价
     */
    private void getAmount(String startTime, String endTime, List<OldKzReportsVO> oldKzReportsVOS, String kzNameOrPhone, String tableInfo, String tableDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT detail.OLDKZNAME oldKzName,detail.OLDKZPHONE oldKzPhone, sum(detail.AMOUNT) as sum_amount ");
        sql.append("FROM ");
        sql.append(tableInfo + "  info ");
        sql.append("LEFT JOIN " + tableDetail + " detail ON info.KZID = detail.KZID ");
        sql.append("where (detail.OLDKZNAME  is not null  or detail.OLDKZPHONE is not null)");
        sql.append(" and info.SuccessTime between ? and ?");
        sql.append(" and (detail.oldKzName like concat('%',?,'%') or detail.oldKzPhone like concat('%',?,'%'))");
        sql.append(" GROUP BY detail.OLDKZPHONE,detail.oldkzname ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{kzNameOrPhone, kzNameOrPhone,startTime, endTime});
        List<OldKzReportsVO> oldKzReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            OldKzReportsVO oldKzReportsVO = new OldKzReportsVO();
            oldKzReportsVO.setAmount(((BigDecimal) map.get("sum_amount")).doubleValue());
            oldKzReportsVO.setOldKzName((String) map.get("oldKzName"));
            oldKzReportsVO.setOldKzPhone((String) map.get("oldKzPhone"));
            oldKzReportsBak.add(oldKzReportsVO);
        }
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            for (OldKzReportsVO oldKzReportsVO1 : oldKzReportsBak) {
                if(StringUtil.isNotEmpty(oldKzReportsVO1.getOldKzPhone())&&StringUtil.isNotEmpty(oldKzReportsVO.getOldKzPhone())){
                    if (oldKzReportsVO.getOldKzPhone().equalsIgnoreCase(oldKzReportsVO1.getOldKzPhone())&&oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName()) ) {
                        oldKzReportsVO.setAmount(oldKzReportsVO1.getAmount());
                        break;
                    }
                }else{
                    if( oldKzReportsVO.getOldKzName().equalsIgnoreCase(oldKzReportsVO1.getOldKzName())){
                        oldKzReportsVO.setAmount(oldKzReportsVO1.getAmount());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 获取基础sql
     *
     * @param sql
     * @param tableInfo
     * @param tableDetail
     */
    private void getBaseSql(StringBuilder sql, String tableInfo, String tableDetail) {
        sql.append("SELECT ");
        sql.append("count(info.KZID) count ,OLDKZPHONE oldKzPhone,OLDKZNAME oldKzName  ");
        sql.append("FROM ");
        sql.append(tableInfo + "  info ");
        sql.append("LEFT JOIN " + tableDetail + " detail ON info.KZID = detail.KZID ");
        sql.append("where (detail.OLDKZNAME  is not null  or detail.OLDKZPHONE is not null)");
        sql.append(" and (detail.oldKzName like concat('%',?,'%') or detail.oldKzPhone like concat('%',?,'%'))");
    }

    /**
     * 计算Rate
     */
    private void computerRate(List<OldKzReportsVO> oldKzReportsVOS, DsInvalidVO invalidConfig) {
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            //有效量
            if (invalidConfig.getDdIsValid()) {
                oldKzReportsVO.setValidClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getInValidClientCount() - oldKzReportsVO.getFilterInClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterPendingClientCount());
            } else {
                oldKzReportsVO.setValidClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getPendingClientCount() - oldKzReportsVO.getInValidClientCount() - oldKzReportsVO.getFilterInClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            oldKzReportsVO.setClientCount(oldKzReportsVO.getAllClientCount() - oldKzReportsVO.getFilterPendingClientCount() - oldKzReportsVO.getFilterInValidClientCount() - oldKzReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) oldKzReportsVO.getValidClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) oldKzReportsVO.getInValidClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) oldKzReportsVO.getPendingClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) oldKzReportsVO.getComeShopClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) oldKzReportsVO.getComeShopClientCount() / oldKzReportsVO.getValidClientCount();
            oldKzReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getClientCount();
            oldKzReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getValidClientCount();
            oldKzReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) oldKzReportsVO.getSuccessClientCount() / oldKzReportsVO.getComeShopClientCount();
            oldKzReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

        }
    }

    /**
     * 计算总计
     */
    private void computerTotal(List<OldKzReportsVO> oldKzReportsVOS) {
        OldKzReportsVO oldKzReportsTotal = new OldKzReportsVO();
        oldKzReportsTotal.setOldKzName("合计");
        for (OldKzReportsVO oldKzReportsVO : oldKzReportsVOS) {
            oldKzReportsTotal.setAllClientCount(oldKzReportsVO.getAllClientCount() + oldKzReportsTotal.getAllClientCount());
            oldKzReportsTotal.setClientCount(oldKzReportsVO.getClientCount() + oldKzReportsTotal.getClientCount());
            oldKzReportsTotal.setValidClientCount(oldKzReportsVO.getValidClientCount() + oldKzReportsTotal.getValidClientCount());
            oldKzReportsTotal.setPendingClientCount(oldKzReportsVO.getPendingClientCount() + oldKzReportsTotal.getPendingClientCount());
            oldKzReportsTotal.setInValidClientCount(oldKzReportsVO.getInValidClientCount() + oldKzReportsTotal.getInValidClientCount());
            oldKzReportsTotal.setComeShopClientCount(oldKzReportsVO.getComeShopClientCount() + oldKzReportsTotal.getComeShopClientCount());
            oldKzReportsTotal.setSuccessClientCount(oldKzReportsVO.getSuccessClientCount() + oldKzReportsTotal.getSuccessClientCount());
            oldKzReportsTotal.setAmount(oldKzReportsVO.getAmount() + oldKzReportsTotal.getAmount());
        }
        //客资量(总客资-筛选待定-筛选中-筛选无效)
        oldKzReportsTotal.setClientCount(oldKzReportsTotal.getAllClientCount() - oldKzReportsTotal.getFilterPendingClientCount() - oldKzReportsTotal.getFilterInValidClientCount() - oldKzReportsTotal.getFilterInClientCount());
        //有效率
        double validRate = (double) oldKzReportsTotal.getValidClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) oldKzReportsTotal.getInValidClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) oldKzReportsTotal.getPendingClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) oldKzReportsTotal.getComeShopClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) oldKzReportsTotal.getComeShopClientCount() / oldKzReportsTotal.getValidClientCount();
        oldKzReportsTotal.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getClientCount();
        oldKzReportsTotal.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getValidClientCount();
        oldKzReportsTotal.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) oldKzReportsTotal.getSuccessClientCount() / oldKzReportsTotal.getComeShopClientCount();
        oldKzReportsTotal.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
        //成交均价
        double avgAmount = oldKzReportsTotal.getAmount() / oldKzReportsTotal.getSuccessClientCount();
        oldKzReportsTotal.setAvgAmount(parseDouble(((Double.isNaN(avgAmount) || Double.isInfinite(avgAmount)) ? 0.0 : avgAmount) * 100));

        oldKzReportsVOS.add(0, oldKzReportsTotal);
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }

}
