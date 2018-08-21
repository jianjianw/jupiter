package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.*;
import org.omg.CORBA.DATA_CONVERSION;
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
    public List<ZjsKzOfMonthShowVO> getzjskzOfMonth(List<SourcePO> sourcePOS,List<Map<String, Object>> dayList, String month, Integer companyId, String tableInfo, String sourceIds, String typeIds,DsInvalidVO dsInvalidVO,String type) {
        String tableDetail=DBSplitUtil.getTable(TableEnum.detail,companyId);
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS=new ArrayList<>();
        for(Map<String, Object> dayMap:dayList){
            for(SourcePO sourcePO:sourcePOS){
                ZjsKzOfMonthOutVO zjsKzOfMonthOutVO=new ZjsKzOfMonthOutVO();
                zjsKzOfMonthOutVO.setDay((String)dayMap.get("day"));
                zjsKzOfMonthOutVO.setDayKey((String)dayMap.get("dayKey"));
                zjsKzOfMonthOutVO.setSrcId(sourcePO.getId());
                zjsKzOfMonthOutVO.setSrcImg(sourcePO.getSrcImg());
                zjsKzOfMonthOutVO.setSrcName(sourcePO.getSrcName());
                zjsKzOfMonthOutVOS.add(zjsKzOfMonthOutVO);
            }
        }
        //总客资
        getAllClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //待定量
        getPendingClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds,dsInvalidVO);
        //筛选待定
        getFilterWaitClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //筛选无效
        getFilterInValidClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //无效
        getInValidClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds,dsInvalidVO);
        //筛选中
        getFilterInClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //入店量
        getComeShopClient1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //成交量
        getSuccessClient1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        computerRate1(zjsKzOfMonthOutVOS,dsInvalidVO);
        List<ZjsKzOfMonthShowVO> zjsKzOfMonthShowVOS=new ArrayList<>();
        groupBy(sourcePOS,zjsKzOfMonthOutVOS,zjsKzOfMonthShowVOS,type, dayList);
        return zjsKzOfMonthShowVOS;
    }

    /**
     * 客资总量
     * @param tableDetail
     * @param tableInfo
     * @param month
     * @param zjsKzOfMonthOutVOS
     * @param sourceIds
     * @param companyId
     * @param typeIds
     */
    private void getAllClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setAllClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setAllClientCount(zjsKzOfMonthOutVO1.getAllClientCount());
                }
            }
        }
    }

    /**
     * 待定量
     */
    private void getPendingClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds,DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setPendingClientCount(zjsKzOfMonthOutVO1.getPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选待定
     */
    private void getFilterWaitClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setFilterPendingClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setFilterPendingClientCount(zjsKzOfMonthOutVO1.getFilterPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setFilterInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setFilterInValidClientCount(zjsKzOfMonthOutVO1.getFilterInValidClientCount());
                }
            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setFilterInClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setFilterInClientCount(zjsKzOfMonthOutVO1.getFilterInClientCount());
                }
            }
        }
    }

    /**
     * 无效量
     */
    private void getInValidClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
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
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setInValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setInValidClientCount(zjsKzOfMonthOutVO1.getInValidClientCount());
                }
            }
        }
    }

    /**
     * 入店量
     */
    private void getComeShopClient1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.ComeShopTime, '%Y/%m') = ?");
        sql.append(" GROUP BY FROM_UNIXTIME(info.ComeShopTime, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setComeShopClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setComeShopClientCount(zjsKzOfMonthOutVO1.getComeShopClientCount());
                }
            }
        }
    }

    /**
     * 成交量
     */
    private void getSuccessClient1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.SuccessTime, '%Y/%m') = ?");
        sql.append(" GROUP BY FROM_UNIXTIME(info.SuccessTime, '%Y/%m/%d')");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setSuccessClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId()==zjsKzOfMonthOutVO1.getSrcId()){
                    zjsKzOfMonthOutVO.setSuccessClientCount(zjsKzOfMonthOutVO1.getSuccessClientCount());
                }
            }
        }
    }

    private void getBaseSql(StringBuilder sql,String tableInfo,String sourceIds,String typeIds,String tableDetail){
        sql.append("SELECT src.srcNAME srcName,");
        sql.append(" src.SRCIMG srcImg ,");
        sql.append(" src.ID srcId,");
        sql.append(" count(info.kzid) count,");
        sql.append(" FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') day");
        sql.append(" FROM  hm_crm_source src");
        sql.append(" LEFT JOIN "+tableInfo+" info ON info.SOURCEID = src.id");
        sql.append(" LEFT JOIN "+tableDetail+" detail ON info.kzid = detail.kzid");
        sql.append(" WHERE src.typeid IN (3, 4, 5)");
        sql.append(" and src.companyId=?");
        sql.append(" and info.isdel = 0");
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append(" and src.id in ("+sourceIds+")");
        }
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" and info.typeid in ("+typeIds+")");
        }
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append("  and info.CLASSID = 1 and info.STATUSID = 98 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and info.CLASSID = 6 and info.STATUSID = 99 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and info.CLASSID = 1 and info.STATUSID = 0 ");
            sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
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
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and FROM_UNIXTIME(info.ComeShopTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
            sql.append(" and info.isdel = 0");
            sql.append(" and info.companyId="+companyId);
            sql.append(" and FROM_UNIXTIME(info.SuccessTime, '%Y/%m/%d')= '" + day.get("day") + "') " + day.get("dayKey") + ",");
        }
        sql.append("(select count(info.id) from " + tableInfo + " info where info.SOURCEID IN ");
        if (StringUtil.isEmpty(sourceId)) {
            sql.append("(select id from hm_crm_source where companyId=" + companyId + " and typeid in ("+typeId+") )");
        } else {
            sql.append("(" + sourceId + ")");
        }
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
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
    private void computerRate1(List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, DsInvalidVO invalidConfig) {
        for (ZjsKzOfMonthOutVO dstgGoldDataReportsVO : zjsKzOfMonthOutVOS) {
            //有效量
            if (invalidConfig.getDdIsValid()) {
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
            } else {
                dstgGoldDataReportsVO.setValidClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getPendingClientCount() - dstgGoldDataReportsVO.getInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgGoldDataReportsVO.setClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount());

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

    private void groupBy(List<SourcePO> sourcePOS,List<ZjsKzOfMonthOutVO> list,List<ZjsKzOfMonthShowVO> showList,String type,List<Map<String, Object>> dayList){

        for(SourcePO sourcePO:sourcePOS ){
            ZjsKzOfMonthShowVO zjsKzOfMonthShowVO=new ZjsKzOfMonthShowVO();
            zjsKzOfMonthShowVO.setSrcId(sourcePO.getId());
            zjsKzOfMonthShowVO.setSrcImg(sourcePO.getSrcImg());
            zjsKzOfMonthShowVO.setSrcName(sourcePO.getSrcName());
            Map<String,Integer> map=new HashMap<>();
            Integer hj=0;
            for(ZjsKzOfMonthOutVO zjsKzOfMonthOutVO:list){

                if(zjsKzOfMonthOutVO.getSrcId()==sourcePO.getId()){
                    if(type.equals("all")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getAllClientCount());
                        hj+=zjsKzOfMonthOutVO.getAllClientCount();
                    }
                    if(type.equals("valid")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidClientCount());
                        hj+=zjsKzOfMonthOutVO.getValidClientCount();
                    }
                    if(type.equals("come")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getComeShopClientCount());
                        hj+=zjsKzOfMonthOutVO.getComeShopClientCount();
                    }
                    if(type.equals("success")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getSuccessClientCount());
                        hj+=zjsKzOfMonthOutVO.getSuccessClientCount();
                    }

                }
            }
            map.put("hj",hj);
            zjsKzOfMonthShowVO.setMap(map);
            showList.add(zjsKzOfMonthShowVO);
        }
        Map<String,Integer> map=new HashMap<>();
        for(Map<String, Object> dayMap:dayList){
            Integer hj=0;
            for (ZjsKzOfMonthShowVO zjsKzOfMonthShowVO:showList)
            {
                hj+=zjsKzOfMonthShowVO.getMap().get(dayMap.get("dayKey"));
            }
            map.put((String)dayMap.get("dayKey"),hj);
        }
        Integer hj=0;
        for(String countKey:map.keySet()){
            hj+=map.get(countKey);
        }
        map.put("hj",hj);
        ZjsKzOfMonthShowVO zjsKzOfMonthShowVO=new ZjsKzOfMonthShowVO();
        zjsKzOfMonthShowVO.setMap(map);
        zjsKzOfMonthShowVO.setSrcName("合计");
        zjsKzOfMonthShowVO.setSrcId(0);
        showList.add(0,zjsKzOfMonthShowVO);
    }

}
