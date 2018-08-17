package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 转介绍月底客资报表
 * author xiangliang
 */
@Repository
public class ZjskzOfMonthDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //获取每天
    public List<Map<String, Object>> getDayOfMonth(Integer year, Integer month, String infoTable) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DATE_FORMAT(@cdate := DATE_ADD(@cdate, INTERVAL - 1 DAY),'%d') dayName,");
        sql.append("CONCAT('Day',DATE_FORMAT(@cdate ,'%d')) dayKey,");
        sql.append("DATE_FORMAT (@cdate, '%Y/%m/%d') day FROM ");
        sql.append("(SELECT @cdate := DATE_ADD('" + lastDayOfMonth + "', INTERVAL + 1 DAY) FROM " + infoTable + ") t0 ");
        sql.append("LIMIT " + lastDayOfMonth.split(CommonConstant.FILE_SEPARATOR)[2]);
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取报表数据
     *
     * @return
     */
    public List<Map<String, Object>> getzjskzOfMonth(List<Map<String, Object>> dayList, String month, Integer companyId, String tableInfo, String sourceIds, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + tableInfo + " info where info.SOURCEID=src.ID AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.ID) from " + tableInfo + " info where info.SOURCEID=src.ID AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("src.SRCIMG srcImg ");
        sql.append("FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (3, 4, 5) ");
        if (StringUtil.isNotEmpty(sourceIds)) {
            sql.append("AND src.ID IN (" + sourceIds + ")");
        }
        String sqlString = sql.toString();
        //入店量
        if (type.equals("come")) {
            sqlString = sqlString.replaceAll("CREATETIME", "ComeShopTime");
        }
        //成交量
        if (type.equals("success")) {
            sqlString = sqlString.replaceAll("CREATETIME", "SuccessTime");
        }
        System.out.println(sqlString);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlString, new Object[]{companyId});
        StringBuilder hjsql = new StringBuilder();
        hjsql.append("SELECT '合计' srcName ,'' srcId,");
        hjsql.append("(select COUNT(info.ID) from " + tableInfo + " info where  FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "' AND info.SOURCEID IN (SELECT src.ID FROM hm_crm_source src WHERE COMPANYID = " + companyId + " AND TYPEID IN (3, 4, 5)");
        if (StringUtil.isNotEmpty(sourceIds)) {
            hjsql.append("AND src.ID IN (" + sourceIds + ")");
        }
        hjsql.append(")) hj,  ");

        for (Map<String, Object> day : dayList) {
            hjsql.append("(select count(info.ID) from " + tableInfo + " info where FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "' AND info.SOURCEID IN (SELECT src.ID FROM hm_crm_source src WHERE COMPANYID = " + companyId + " AND TYPEID IN (3, 4, 5)");
            if (StringUtil.isNotEmpty(sourceIds)) {
                hjsql.append("AND src.ID IN (" + sourceIds + ")");
            }
            hjsql.append(")) " + day.get("dayKey") + ",");
        }
        hjsql.append("'' srcImg ");
        sqlString = hjsql.toString();
        //入店量
        if (type.equals("come")) {
            sqlString = sqlString.replaceAll("CREATETIME", "ComeShopTime");
        }
        //成交量
        if (type.equals("success")) {
            sqlString = sqlString.replaceAll("CREATETIME", "SuccessTime");
        }
        System.out.println(sqlString);
        List<Map<String, Object>> hjList = jdbcTemplate.queryForList(sqlString, new Object[]{});
        hjList.addAll(list);
        return hjList;
    }

    /**
     * 获取报表详情数据
     */
    public ZjskzOfMonthMapVO ZjskzOfMonthIn(List<Map<String, Object>> dayList, Integer companyId, String month, String sourceId, DsInvalidVO dsInvalidVO,String typeId) {
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS = new ArrayList<>();
        String tableInfo = DBSplitUtil.getTable(TableEnum.info, companyId);
        //总客资
        getAllClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        //待定
        getPendingClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId, dsInvalidVO,typeId);
        //筛选待定
        getFilterWaitClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        //筛选无效
        getFilterInValidClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        //无效
        getInValidClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId, dsInvalidVO,typeId);
        //筛选中
        getFilterInClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        //入店量
        getComeShopClient(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        //成交量
        getSuccessClient(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId,typeId);
        computerRate(zjskzOfMonthReportsVOS, dsInvalidVO);
        ZjskzOfMonthMapVO zjskzOfMonthMapVO = new ZjskzOfMonthMapVO();
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String, Object> clientCountMap = new HashMap<>();
        clientCountMap.put("name", "客资量");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            clientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getClientCount());
        }
        list.add(clientCountMap);
        Map<String, Object> validClientCountMap = new HashMap<>();
        validClientCountMap.put("name", "有效量");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            validClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getValidClientCount());
        }
        list.add(validClientCountMap);
        Map<String, Object> comeShopClientCountMap = new HashMap<>();
        comeShopClientCountMap.put("name", "入店量");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            comeShopClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getComeShopClientCount());
        }
        list.add(comeShopClientCountMap);
        Map<String, Object> successClientCountMap = new HashMap<>();
        successClientCountMap.put("name", "成交量");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            successClientCountMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getSuccessClientCount());
        }
        list.add(successClientCountMap);
        Map<String, Object> validRateMap = new HashMap<>();
        validRateMap.put("name", "有效率");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            validRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getValidRate());
        }
        list.add(validRateMap);
        Map<String, Object> validClientComeShopRateMap = new HashMap<>();
        validClientComeShopRateMap.put("name", "入店率");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            validClientComeShopRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getValidClientComeShopRate());
        }
        list.add(validClientComeShopRateMap);
        Map<String, Object> comeShopSuccessRateMap = new HashMap<>();
        comeShopSuccessRateMap.put("name", "成交率");
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            comeShopSuccessRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getComeShopSuccessRate());
        }
        list.add(comeShopSuccessRateMap);
        zjskzOfMonthMapVO.setList(list);
        return zjskzOfMonthMapVO;
    }

    /**
     * 总客资
     */
    private void getAllClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsVOS.add(zjskzOfMonthReportsVO);
            }
        }
    }

    /**
     * 待定量
     */
    private void getPendingClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, DsInvalidVO dsInvalidVO,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setPendingClientCount(zjskzOfMonthReportsVO1.getPendingClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 筛选待定
     */
    private void getFilterWaitClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setFilterPendingClientCount(zjskzOfMonthReportsVO1.getFilterPendingClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setFilterInValidClientCount(zjskzOfMonthReportsVO1.getFilterInValidClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and  info.CLASSID = 1 and info.STATUSID = 0 ");
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setFilterInClientCount(zjskzOfMonthReportsVO1.getFilterInClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 无效量
     */
    private void getInValidClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, DsInvalidVO dsInvalidVO,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        String tableDetail = DBSplitUtil.getTable(TableEnum.detail, companyId);
        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info left join " + tableDetail + " detail on detail.kzid=info.kzid  where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
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
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info left join " + tableDetail + " detail on detail.kzid=info.kzid   where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
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
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setInValidClientCount(zjskzOfMonthReportsVO1.getInValidClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 入店量
     */
    private void getComeShopClient(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and FROM_UNIXTIME(info.ComeShopTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and FROM_UNIXTIME(info.ComeShopTime, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setComeShopClientCount(zjskzOfMonthReportsVO1.getComeShopClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 成交量
     */
    private void getSuccessClient(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId,String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
            if (StringUtil.isEmpty(sourceId)) {
                sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
            } else {
                sql.append("(" + sourceId + ")");
            }
            sql.append(" and FROM_UNIXTIME(info.SuccessTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and FROM_UNIXTIME(info.SuccessTime, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setSuccessClientCount(zjskzOfMonthReportsVO1.getSuccessClientCount());
                    break;
                }
            }
        }
    }

    /**
     * 计算Rate
     */
    private void computerRate(List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, DsInvalidVO invalidConfig) {
        for (ZjskzOfMonthReportsVO dstgGoldDataReportsVO : zjskzOfMonthReportsVOS) {
            //有效量
            if (invalidConfig.getDdIsValid()) {
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
            } else {
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getPendingClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgGoldDataReportsVO.setClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) dstgGoldDataReportsVO.getValidClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));

            //毛客资入店率
            double clientComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));

            //入店成交率
            double comeShopSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getComeShopClientCount();
            dstgGoldDataReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));

        }
    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }
}
