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

import java.math.BigDecimal;
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
        sql.append("(SELECT @cdate := DATE_ADD('" + lastDayOfMonth + "', INTERVAL + 1 DAY) FROM hm_crm_dictionary) t0 ");
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
        //有效
        getValidClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds,dsInvalidVO);
        //筛选中
        getFilterInClientCount1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //入店量
        getComeShopClient1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        //成交量
        getSuccessClient1(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        getAmount(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        getAvgAmount(tableDetail,tableInfo,month,zjsKzOfMonthOutVOS,sourceIds,companyId,typeIds);
        getCost(month,companyId,zjsKzOfMonthOutVOS);
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
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d'),info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
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
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d'),info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
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
        sql.append("     and info.STATUSID = 98 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d'),info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
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
        sql.append("   and info.STATUSID = 99 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d'),info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
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
        sql.append("    and info.STATUSID = 0 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d'),info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setFilterInClientCount(zjsKzOfMonthOutVO1.getFilterInClientCount());
                }
            }
        }
    }

    /**
     *  有效
     */
    private void getValidClientCount1(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds, DsInvalidVO dsInvalidVO) {
        StringBuilder sql = new StringBuilder();
        getBaseSql(sql, tableInfo,sourceIds,typeIds,tableDetail);
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" AND INSTR('"+dsInvalidVO.getZjsValidStatus()+"',CONCAT( ','+info.STATUSID + '', ','))>0 ");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') ,info.sourceId");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setValidClientCount(Integer.parseInt(Long.toString((Long) (map.get("count")))));
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setValidClientCount(zjsKzOfMonthOutVO1.getValidClientCount());
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
        sql.append(" GROUP BY FROM_UNIXTIME(info.ComeShopTime, '%Y/%m/%d') ,info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
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
        sql.append(" GROUP BY FROM_UNIXTIME(info.SuccessTime, '%Y/%m/%d') ,info.sourceId");
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
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setSuccessClientCount(zjsKzOfMonthOutVO1.getSuccessClientCount());
                }
            }
        }
    }
    /**
     * 成交均价
     */
    private void getAvgAmount(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT src.srcNAME srcName,");
        sql.append(" src.SRCIMG srcImg ,");
        sql.append(" src.ID srcId,");
        sql.append("  avg(detail.AMOUNT) count,");
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
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" and detail.AMOUNT is not null");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') ,info.sourceId");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setAvgAmount(((BigDecimal) map.get("count")).doubleValue());
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setAvgAmount(zjsKzOfMonthOutVO1.getAvgAmount());
                }
            }
        }
    }

    /**
     * 成交总价
     */
    private void getAmount(String tableDetail,String tableInfo, String month, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS, String sourceIds, Integer companyId,String typeIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT src.srcNAME srcName,");
        sql.append(" src.SRCIMG srcImg ,");
        sql.append(" src.ID srcId,");
        sql.append("  sum(detail.AMOUNT) count,");
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
        sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m') = ?");
        sql.append(" and detail.AMOUNT is not null");
        sql.append(" GROUP BY FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') ,info.sourceId");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setAmount(((BigDecimal) map.get("count")).doubleValue());
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setAmount(zjsKzOfMonthOutVO1.getAmount());
                }
            }
        }
    }

    /**
     * 花费
     */
    private void getCost(String month,Integer companyId, List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutVOS){
        StringBuilder sql=new StringBuilder();
        sql.append(" SELECT src.ID srcId,");
        sql.append(" cost.COST cost,");
        sql.append(" FROM_UNIXTIME(cost.COSTTIME, '%Y/%m/%d') day");
        sql.append(" FROM hm_crm_cost cost");
        sql.append(" LEFT JOIN hm_crm_source src ON src.ID = cost.SRCID");
        sql.append(" AND src.COMPANYID = cost.COMPANYID");
        sql.append(" WHERE cost.companyid = ? ");
        sql.append(" and src.ID is not null");
        sql.append(" AND FROM_UNIXTIME(cost.COSTTIME, '%Y/%m') =  ? ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId,month});
        List<ZjsKzOfMonthOutVO> zjsKzOfMonthOutBzk = new LinkedList<>();
        for (Map<String, Object> map : list) {
            ZjsKzOfMonthOutVO zjsKzOfMonthOutVO  = new ZjsKzOfMonthOutVO();
            zjsKzOfMonthOutVO.setDay((String)map.get("day"));
            zjsKzOfMonthOutVO.setSrcId(Integer.parseInt(Long.toString((Long) (map.get("srcId")))));
            zjsKzOfMonthOutVO.setAllCost(map.get("cost")+"");
            zjsKzOfMonthOutBzk.add(zjsKzOfMonthOutVO);
        }
        for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO : zjsKzOfMonthOutVOS) {
            for (ZjsKzOfMonthOutVO zjsKzOfMonthOutVO1 : zjsKzOfMonthOutBzk) {
                if(zjsKzOfMonthOutVO.getDay().equalsIgnoreCase(zjsKzOfMonthOutVO1.getDay())&&zjsKzOfMonthOutVO.getSrcId().equals(zjsKzOfMonthOutVO1.getSrcId())){
                    zjsKzOfMonthOutVO.setAllCost(zjsKzOfMonthOutVO1.getAllCost());
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
        //有效
        getValidClientCount(dayList, tableInfo, month, zjskzOfMonthReportsVOS, sourceId, companyId, dsInvalidVO,typeId);
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
            validClientComeShopRateMap.put(zjskzOfMonthReportsVO.getDayId(), zjskzOfMonthReportsVO.getClientComeShopRate());
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
            sql.append("     and info.STATUSID = 98 ");
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
        sql.append("     and info.STATUSID = 98 ");
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
            sql.append("   and info.STATUSID = 99 ");
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
        sql.append("   and info.STATUSID = 99 ");
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
            sql.append("    and info.STATUSID = 0 ");
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
        sql.append("  and info.STATUSID = 0 ");
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
     * 有效量
     */
    private void getValidClientCount(List<Map<String, Object>> dayList, String tableInfo, String month, List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, String sourceId, Integer companyId, DsInvalidVO dsInvalidVO,String typeId) {
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
            sql.append(" AND INSTR('"+dsInvalidVO.getZjsValidStatus()+"',CONCAT( ','+info.STATUSID + '', ','))>0 ");
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
        sql.append(" AND INSTR('"+dsInvalidVO.getZjsValidStatus()+"',CONCAT( ','+info.STATUSID + '', ','))>0 ");
        sql.append(" and info.isdel = 0");
        sql.append(" and info.companyId="+companyId);
        sql.append(" and FROM_UNIXTIME(info.CREATETIME, '%Y/%m')= '" + month + "') " + "hj ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{});
        List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsBak = new LinkedList<>();
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                ZjskzOfMonthReportsVO zjskzOfMonthReportsVO = new ZjskzOfMonthReportsVO();
                zjskzOfMonthReportsVO.setDayId(key);
                zjskzOfMonthReportsVO.setValidClientCount(Integer.parseInt(Long.toString((Long) (map.get(key)))));
                zjskzOfMonthReportsBak.add(zjskzOfMonthReportsVO);
            }
        }
        for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO : zjskzOfMonthReportsVOS) {
            for (ZjskzOfMonthReportsVO zjskzOfMonthReportsVO1 : zjskzOfMonthReportsBak) {
                if (zjskzOfMonthReportsVO.getDayId().equalsIgnoreCase(zjskzOfMonthReportsVO1.getDayId())) {
                    zjskzOfMonthReportsVO.setValidClientCount(zjskzOfMonthReportsVO1.getValidClientCount());
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
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            dstgGoldDataReportsVO.setClientCount(dstgGoldDataReportsVO.getAllClientCount() - dstgGoldDataReportsVO.getFilterPendingClientCount() - dstgGoldDataReportsVO.getFilterInValidClientCount() - dstgGoldDataReportsVO.getFilterInClientCount());
            //有效率
            double validRate = (double) dstgGoldDataReportsVO.getValidClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
            //无效率
            double invalidRate = (double) dstgGoldDataReportsVO.getInValidClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
            //待定率
            double waitRate = (double) dstgGoldDataReportsVO.getPendingClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
            //毛客资入店率
            double clientComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
            //有效客资入店率
            double validComeShopRate = (double) dstgGoldDataReportsVO.getComeShopClientCount() / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
            //毛客资成交率
            double successRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
            //有效客资成交率
            double validSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
            //入店成交率
            double comeShopSuccessRate = (double) dstgGoldDataReportsVO.getSuccessClientCount() / dstgGoldDataReportsVO.getComeShopClientCount();
            dstgGoldDataReportsVO.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
            //毛客资成本
            double clientCost = Double.valueOf(StringUtil.isEmpty(dstgGoldDataReportsVO.getAllCost()) ? "0" : dstgGoldDataReportsVO.getAllCost()) / dstgGoldDataReportsVO.getClientCount();
            dstgGoldDataReportsVO.setClientCost(String.valueOf(parseDouble((Double.isNaN(clientCost) || Double.isInfinite(clientCost)) ? 0.0 : clientCost)));
            //有效客资成本
            double validClientCost = Double.valueOf(StringUtil.isEmpty(dstgGoldDataReportsVO.getAllCost()) ? "0" : dstgGoldDataReportsVO.getAllCost()) / dstgGoldDataReportsVO.getValidClientCount();
            dstgGoldDataReportsVO.setValidClientCost(String.valueOf(parseDouble((Double.isNaN(validClientCost) || Double.isInfinite(validClientCost)) ? 0.0 : validClientCost)));
            //入店成本
            double appointClientCost = Double.valueOf(StringUtil.isEmpty(dstgGoldDataReportsVO.getAllCost()) ? "0" : dstgGoldDataReportsVO.getAllCost()) / dstgGoldDataReportsVO.getComeShopClientCount();
            dstgGoldDataReportsVO.setComeShopClientCost(String.valueOf(parseDouble((Double.isNaN(appointClientCost) || Double.isInfinite(appointClientCost)) ? 0.0 : appointClientCost)));
            //成交成本
            double successClientCost = Double.valueOf(StringUtil.isEmpty(dstgGoldDataReportsVO.getAllCost()) ? "0" : dstgGoldDataReportsVO.getAllCost()) / dstgGoldDataReportsVO.getSuccessClientCount();
            dstgGoldDataReportsVO.setSuccessClientCost(String.valueOf(parseDouble((Double.isNaN(successClientCost) || Double.isInfinite(successClientCost)) ? 0.0 : successClientCost)));
            // ROI
            double roi = dstgGoldDataReportsVO.getAmount() / Double.valueOf(StringUtil.isEmpty(dstgGoldDataReportsVO.getAllCost()) ? "0" : dstgGoldDataReportsVO.getAllCost());
            dstgGoldDataReportsVO.setROI(String.valueOf(parseDouble(((Double.isNaN(roi) || Double.isInfinite(roi)) ? 0.0 : roi) * 100)));
        }
    }
    /**
     * 计算Rate
     */
    private void computerRate(List<ZjskzOfMonthReportsVO> zjskzOfMonthReportsVOS, DsInvalidVO invalidConfig) {
        for (ZjskzOfMonthReportsVO dstgGoldDataReportsVO : zjskzOfMonthReportsVOS) {
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
            dstgGoldDataReportsVO.setValidClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));

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
            Map<String,String> map=new HashMap<>();
            double hj=0;
            int i=0;
            for(ZjsKzOfMonthOutVO zjsKzOfMonthOutVO:list){

                if(zjsKzOfMonthOutVO.getSrcId()==sourcePO.getId()){
                    if(type.equals("all")) {
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getClientCount()+"");
                        hj += zjsKzOfMonthOutVO.getClientCount();
                    }else if(type.equals("valid")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getValidClientCount();
                    }else if(type.equals("come")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getComeShopClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getComeShopClientCount();
                    }else if(type.equals("success")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getSuccessClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getSuccessClientCount();
                    }else if(type.equals("sum")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getAllClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getAllClientCount();
                    }else if(type.equals("invalid")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getInValidClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getInValidClientCount();
                    }else if(type.equals("ddnum")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getPendingClientCount()+"");
                        hj+=zjsKzOfMonthOutVO.getPendingClientCount();
                    }else if(type.equals("validrate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidRate()+"");
                        if(zjsKzOfMonthOutVO.getValidRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getValidRate();
                            i++;
                        }
                    }else if(type.equals("ddrate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getWaitRate()+"");
                        if(zjsKzOfMonthOutVO.getWaitRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getWaitRate();
                            i++;
                        }
                    }else if(type.equals("allcomerate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getClientComeShopRate()+"");
                        if(zjsKzOfMonthOutVO.getClientComeShopRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getClientComeShopRate();
                            i++;
                        }
                    }else if(type.equals("validcomerate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidClientComeShopRate()+"");
                        if(zjsKzOfMonthOutVO.getValidClientComeShopRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getValidClientComeShopRate();
                            i++;
                        }
                    }else if(type.equals("rdsuccessrate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getComeShopSuccessRate()+"");
                        if(zjsKzOfMonthOutVO.getComeShopSuccessRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getComeShopSuccessRate();
                            i++;
                        }
                    }else if(type.equals("allsuccessrate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getClientSuccessRate()+"");
                        if(zjsKzOfMonthOutVO.getClientSuccessRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getClientSuccessRate();
                            i++;
                        }
                    }else if(type.equals("validsuccessrate")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidClientSuccessRate()+"");
                        if(zjsKzOfMonthOutVO.getValidClientSuccessRate()!=0){
                            hj+=zjsKzOfMonthOutVO.getValidClientSuccessRate();
                            i++;
                        }
                    }else if(type.equals("amount")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getAllCost());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getAllCost());
                    }else if(type.equals("allcb")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getClientCost());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getClientCost());
                    }else if(type.equals("validcb")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getValidClientCost());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getValidClientCost());
                    }else if(type.equals("comecb")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getComeShopClientCost());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getComeShopClientCost());
                    }else if(type.equals("successcb")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getSuccessClientCost());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getSuccessClientCost());
                    }else if(type.equals("successavg")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getAvgAmount()+"");
                        hj+=zjsKzOfMonthOutVO.getAvgAmount();
                    }else if(type.equals("successamount")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getAmount()+"");
                        hj+=zjsKzOfMonthOutVO.getAmount();
                    }else if(type.equals("roi")){
                        map.put(zjsKzOfMonthOutVO.getDayKey(),zjsKzOfMonthOutVO.getROI());
                        hj+=Double.parseDouble(zjsKzOfMonthOutVO.getROI());
                    }


                }
            }
            if(type.equals("all")||type.equals("come")||type.equals("success")||type.equals("sum")||type.equals("invalid")||type.equals("ddnum")||type.equals("valid")){
                map.put("hj",((int)hj)+"");
            }else if(type.equals("validrate")||type.equals("ddrate")||type.equals("allcomerate")||type.equals("validcomerate")||type.equals("rdsuccessrate")||type.equals("allsuccessrate")||type.equals("validsuccessrate")){
                if(i==0){
                    map.put("hj",parseDouble(hj)+"");
                }else {
                    map.put("hj", parseDouble(hj / i) + "");
                }
            }else{
                map.put("hj",parseDouble(hj)+"");
            }

            zjsKzOfMonthShowVO.setMap(map);
            showList.add(zjsKzOfMonthShowVO);
        }

         Map<String,String> map=new HashMap<>();
        for(Map<String, Object> dayMap:dayList){
            double hj=0;
            int i=0;
            for (ZjsKzOfMonthShowVO zjsKzOfMonthShowVO:showList)
            {
                hj+=Double.parseDouble(zjsKzOfMonthShowVO.getMap().get(dayMap.get("dayKey")));
                if(Double.parseDouble(zjsKzOfMonthShowVO.getMap().get(dayMap.get("dayKey")))!=0){
                    i++;
                }
            }
            if(type.equals("all")||type.equals("come")||type.equals("success")||type.equals("sum")||type.equals("invalid")||type.equals("ddnum")||type.equals("valid")){
                map.put((String)dayMap.get("dayKey"),((int)hj)+"");
            }else if(type.equals("validrate")||type.equals("ddrate")||type.equals("allcomerate")||type.equals("validcomerate")||type.equals("rdsuccessrate")||type.equals("allsuccessrate")||type.equals("validsuccessrate")){
                if(i==0){
                    map.put((String)dayMap.get("dayKey"),parseDouble(hj)+"");
                }else {
                    map.put((String)dayMap.get("dayKey"), parseDouble(hj / i) + "");
                }
            }else{
                map.put("hj",parseDouble(hj)+"");
            }
        }
        double hj=0;
        int i=0;
        for(String countKey:map.keySet()){
            hj+=Double.parseDouble(map.get(countKey));
            if(Double.parseDouble(map.get(countKey))!=0){
                i++;
            }
        }
        if(type.equals("all")||type.equals("come")||type.equals("success")||type.equals("sum")||type.equals("invalid")||type.equals("ddnum")||type.equals("valid")){
            map.put("hj",((int)hj)+"");
        }else if(type.equals("validrate")||type.equals("ddrate")||type.equals("allcomerate")||type.equals("validcomerate")||type.equals("rdsuccessrate")||type.equals("allsuccessrate")||type.equals("validsuccessrate")){
            if(i==0){
                map.put("hj",parseDouble(hj)+"");
            }else {
                map.put("hj", parseDouble(hj / i) + "");
            }
        }else{
            map.put("hj",parseDouble(hj)+"");
        }
        ZjsKzOfMonthShowVO zjsKzOfMonthShowVO=new ZjsKzOfMonthShowVO();
        zjsKzOfMonthShowVO.setMap(map);
        zjsKzOfMonthShowVO.setSrcName("合计");
        zjsKzOfMonthShowVO.setSrcId(0);
        showList.add(0,zjsKzOfMonthShowVO);

    }

}
