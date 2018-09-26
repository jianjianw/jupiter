package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author: shiTao
 */
public class TimeUtilTest {

    @Test
    public void getMonthStartByDate() {
//        Date monthStartByDate = TimeUtil.getMonthStartByDate("2018-03");
//        System.out.println(TimeUtil.getTimeStamp10ByDate(monthStartByDate));
    }

    @Test
    public void getTimeStamp10ByDate() {
    }

    @Test
    public void getMonthEndByDate() {
//        Date monthStartByDate = TimeUtil.getMonthEndByDate("2018-01");
//        System.out.println(TimeUtil.getDayLastTimeStamp10ByDate(monthStartByDate));
    }


    @Test
    public void testgetData() {
        Map<String, Integer> map = new HashMap<>();
        Calendar calendar1 = Calendar.getInstance();
        //设置昨天
        calendar1.setTime(new Date());
        calendar1.set(Calendar.HOUR, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        map.put("start", TimeUtil.getTimeStamp10ByDate(calendar1.getTime()));
        map.put("end", TimeUtil.getTimeStamp10ByDate(calendar2.getTime()));
        System.out.println(JSON.toJSONString(map));
    }
}