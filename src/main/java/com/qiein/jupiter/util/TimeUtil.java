package com.qiein.jupiter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间处理工具
 *
 * @author JingChenglong 2017/12/16 13:41
 */
public class TimeUtil {

    private final static Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 时间格式化格式
     */
    public static SimpleDateFormat ymdSDF_ = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat ymdSDFLeft = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat ymdSDFNoCut = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat yyyyMMddHHmmss_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yyyyMMddHHmmssLeft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static SimpleDateFormat yyyyMMddHHmmssNoCut = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat yyyyMMddHHmmssSSS_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static SimpleDateFormat yyyyMMddHHmmssSSSLeft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SSS");
    public static SimpleDateFormat yyyyMMddHHmmssSSSNoCut = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 根据指定时间格式格式化系统时间
     *
     * @param format
     * @return
     */
    public static String getSysTime(String format) {
        return formatSysTime(new SimpleDateFormat(format));
    }

    /**
     * 根据指定格式格式化当前系统时间
     *
     * @param sdf
     * @return
     */
    public static String formatSysTime(SimpleDateFormat sdf) {
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前时间int格式值： yyyyMMddHHmmssSSS
     */
    public static String getTimeInt() {
        return yyyyMMddHHmmssSSSNoCut.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期int格式值： yyyyMMdd
     */
    public static String getDate() {
        return ymdSDFNoCut.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前系统时间精确值：yyyy-MM-dd HH:mm:ss,SSS
     */
    public static String getTime() {
        return yyyyMMddHHmmssSSS_.format(Calendar.getInstance().getTime());
    }

    /**
     * 根据指定格式格式化指定时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.ENGLISH).format(date);
    }

    /**
     * 当前月份加一个月
     */
    public static Date getNextMonth() {
        return addMonth(new Date(), 1);
    }

    /**
     * 当前月份减一个月
     */
    public static Date getLastMonth() {
        return addMonth(new Date(), -1);
    }

    /**
     * 指定日期(时间)+-月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, int month) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 获取当前时间昨天时间
     */
    public static Date getYesterDay() {
        return getYesterDay(new Date());
    }

    /**
     * 指定时间减一天
     *
     * @param date
     * @return
     */
    public static Date getYesterDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取去年的当前时间
     */
    public static Date getLastYear() {
        return addYear(new Date(), -1);
    }

    /**
     * 获取明年的当前时间
     */
    public static Date getNextYear() {
        return addYear(new Date(), 1);
    }

    /**
     * 指定时间+-月
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date, int year) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 指定时间+-指定天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDate(Date date, int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 指定时间+-指定小时
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date getDateByHour(Date date, int hours) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    /**
     * 指定时间+=指定秒
     *
     * @param date
     * @param second
     * @return
     */
    public static Date getDateBySecond(Date date, int seconds) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 校验时间格式
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static boolean validTime(String dateStr, String pattern) {

        if (StringUtil.haveEmpty(dateStr, pattern)) {
            return false;
        }

        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            logger.error(CommonUtils.getTraceInfo() + e.getMessage());
            return false;
        }

        return dateStr.equals(df.format(date));
    }

    /**
     * 将指定时间字符串及给定的格式格式化为指定时间
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date format(String dateStr, String pattern) {

        if (StringUtil.haveEmpty(dateStr, pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);

        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            logger.error(CommonUtils.getTraceInfo() + e.getMessage());
            return null;
        }

        return date;
    }

    /**
     * 将指定时间格式字符串根据给定的时间格式格式化为给定的新的时间格式
     *
     * @param dateStr
     * @param oldSdf
     * @param newSdf
     * @return
     */
    public static String format(String dateStr, SimpleDateFormat oldSdf, SimpleDateFormat newSdf) {
        if (StringUtil.isEmpty(dateStr) || StringUtil.haveEmpty(oldSdf, newSdf)) {
            return "";
        }

        Date date = null;

        try {
            date = oldSdf.parse(dateStr);
            dateStr = newSdf.format(date);
        } catch (ParseException e) {
            logger.error(CommonUtils.getTraceInfo() + e.getMessage());
            return "";
        } catch (Exception e) {
            logger.error(CommonUtils.getTraceInfo() + e.getMessage());
            return "";
        }

        return dateStr;
    }

    /**
     * 获取当前系统时间： yyyy-MM-dd HH:mm:ss
     */
    public static String getSysTime() {
        return getSysTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前系统时间毫秒级：yyyy-MM-dd HH:mm:ss,SSS
     */
    public static String getSysTimeS() {
        return getSysTime("yyyy-MM-dd HH:mm:ss,SSS");
    }

    /**
     * 获取当前系统时间秒级长整数：yyyyMMddHHmmss
     */
    public static String getSysTimeLong() {
        return getSysTime("yyyyMMddHHmmss");
    }

    /**
     * 获取当前系统时间毫秒级长整数：yyyyMMddHHmmssSSS
     */
    public static String getSysTimeSLong() {
        return getSysTime("yyyyMMddHHmmssSSS");
    }

    /**
     * 获取当前系统日期：yyyy-MM-dd
     */
    public static String getSysdate() {
        return getSysTime("yyyy-MM-dd");
    }

    /**
     * 获取当前系统时间年月日整数：yyyyMMdd
     */
    public static String getSysdateInt() {
        return getSysTime("yyyyMMdd");
    }

    /**
     * 获取当前年份
     */
    public static String getSysYearInt() {
        return getSysTime("yyyy");
    }

    /**
     * 获取当前系统时间年月整数值：yyyyMM
     */
    public static String getSysyearmonthInt() {
        return getSysTime("yyyyMM");
    }

    /**
     * 获取当前系统时间零点
     */
    public static String getSysdateStart() {
        return (getSysdate() + " 00:00:00");
    }

    /**
     * 获取当前系统时间最当天最后一秒
     */
    public static String getSysdateEnd() {
        return (getSysdate() + " 23:59:59");
    }

    /**
     * 获得当前系统时间增加指定秒数后的时间
     *
     * @param second
     * @return
     */
    public static Date getSecond(int second) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, second);

        return cal.getTime();
    }

    /**
     * 当前线程挂起指定毫秒
     *
     * @param millis
     */

    public static void sleep(long millis) {
        if (millis < 0L) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error(CommonUtils.getTraceInfo() + e.getMessage());
        }
    }

    /**
     * 获取系统当前时间戳
     *
     * @return
     */
    public static int getSystemInt() {

        return dateToIntMillis(new Date());
    }

    /**
     * 获取时间对应时间戳
     *
     * @param date
     * @return
     */
    public static int dateToIntMillis(Date date) {

        if (date == null) {
            date = new Date();
        }
        return (int) (date.getTime() / 1000);
    }

    /**
     * 获取时间对应时间戳
     *
     * @param time
     * @return
     */
    public static int dateToIntMillis(String time) {

        return dateToIntMillis(time, yyyyMMddHHmmss_);
    }

    /**
     * 获取时间对应时间戳
     *
     * @param time
     * @param pattern
     * @return
     */
    public static int dateToIntMillis(String time, String pattern) {

        return dateToIntMillis(time, new SimpleDateFormat(pattern));
    }

    /**
     * 获取时间对应时间戳
     *
     * @param time
     * @param format
     * @return
     */
    public static int dateToIntMillis(String time, SimpleDateFormat format) {

        if (StringUtil.isEmpty(time)) {
            time = getSysTime();
        }
        if (format == null) {
            format = yyyyMMddHHmmss_;
        }

        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return dateToIntMillis(date);
    }

    /**
     * 时间戳转换为时间
     *
     * @param second
     * @param format
     * @return
     */
    public static String intMillisToTimeStr(int second, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(second * 1000L));
    }

    /**
     * 时间戳转换为时间
     *
     * @param second
     * @param format
     * @return
     */
    public static String intMillisToTimeStr(int second, SimpleDateFormat format) {

        if (format == null) {
            format = yyyyMMddHHmmssLeft;
        }

        return format.format(new Date(second * 1000L));
    }

    /**
     * 时间戳转换为时间
     *
     * @param second
     * @return
     */
    public static String intMillisToTimeStr(int second) {
        if (second == 0) {
            return "";
        }
        return intMillisToTimeStr(second, yyyyMMddHHmmssLeft);
    }
}