package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.StringUtil;
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
     * @return
     */
    public List<Map<String, Object>> getzjskzOfMonth(List<Map<String, Object>> dayList,String month,Integer companyId,String tableInfo,String sourceIds){
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT src.SRCNAME,");
        sql.append("(select COUNT(info.ID) from "+tableInfo+" info where info.SOURCEID=src.ID AND FROM_UNIXTIME(CREATETIME, '%Y/%m')='"+month+"') hj,  ");
        for(Map<String,Object> day:dayList){
            sql.append("(select count(info.ID) from "+tableInfo+" info where info.SOURCEID=src.ID AND FROM_UNIXTIME(CREATETIME, '%Y/%m/%d')='"+day.get("day")+"') "+day.get("dayKey")+",");
        }
        sql.append("src.SRCIMG ");
        sql.append("FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (3, 4, 5) ");
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append("AND src.ID IN ("+sourceIds+")");
        }
        System.out.println(sql.toString());
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[]{companyId});
        StringBuilder hjsql=new StringBuilder();
        return list;
    }
}
