package com.qiein.jupiter.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

/**
 * 数据库工具类
 *
 * @author JingChenglong 2017-01-19 11:30
 */
public class DBSplitUtil {

    public static final String TAB_SUFFIX_INFO = " hm_crm_client_info_";// 客资信息表
    public static final String TAB_SUFFIX_DETAIL = " hm_crm_client_detail_";// 客资详细信息表
    public static final String TAB_SUFFIX_REMARK = "hm_crm_client_remark_";// 备注富文本
    public static final String TAB_SUFFIX_LOG = " hm_crm_client_log_";// 客资日志表
    public static final String TAB_SUFFIX_INVITA_LOG = " hm_crm_invitation_log_";// 客资邀约记录表
    public static final String TAB_SUFFIX_SHOP_LOG = " hm_crm_shop_meet_log_";// 客资洽谈记录表
    public static final String TAB_SUFFIX_TEMP = " hm_crm_client_info_temp_";// 客资临时表
    public static final String TAB_SUFFIX_IP_LOG = " hm_crm_ip_log_";// ip日志表
    public static final String TAB_SUFFIX_TIMER = " hm_crm_timer_";// 定时任务表
    public static final String TAB_SUFFIX_NEWS = " hm_crm_news_";// 消息表
    public static final String TAB_SUFFIX_GOLD_TEMP = " hm_crm_gold_data_temp_";// 金数据缓存表
    public static final String TAB_SUFFIX_ALLOT_LOG = " hm_crm_allot_log_";// 客资分配日志表
    public static final String TAB_SUFFIX_LINE_LOG = "hm_crm_line_log_";// 上下线日志表
    public static final String TAB_SUFFIX_ACCEPT_LOG = "hm_crm_accept_log_";// 接单日志
    public static final String TAB_SYSLOG_REMARK = "hm_pub_system_log_";// 接单日志
    public static final String TAB_ONLINE_TIME = "hm_crm_online_time_log_";// 在线时长日志

    public static String getNewsTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.news, companyId);
    }

    public static String getInfoTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.info, companyId);
    }

    public static String getAllotLogTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.allot, companyId);
    }

    public static String getSystemLogTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.syslog, companyId);
    }

    public static String getDetailTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.detail, companyId);
    }

    public static String getRemarkTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.remark, companyId);
    }

    public static String getInfoLogTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }

        return getTable(TableEnum.log, companyId);
    }

    public static String getOnLineTimeLogTabName(int companyId) throws RException {

        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANYID_NULL);
        }
        return getTable(TableEnum.online_time, companyId);
    }

    /*-- 获取表名 --*/
    public static String getTable(TableEnum tableEnum, int companyId) {

        if (tableEnum == null || NumUtil.isInValid(companyId)) {
            return "";
        }

        String tableName = "";
        switch (tableEnum) {
            case info:
                tableName = TAB_SUFFIX_INFO + String.valueOf(companyId);
                break;
            case log:
                tableName = TAB_SUFFIX_LOG + String.valueOf(companyId);
                break;
            case invita:
                tableName = TAB_SUFFIX_INVITA_LOG + String.valueOf(companyId);
                break;
            case shopmeet:
                tableName = TAB_SUFFIX_SHOP_LOG + String.valueOf(companyId);
                break;
            case detail:
                tableName = TAB_SUFFIX_DETAIL + String.valueOf(companyId);
                break;
            case temp:
                tableName = TAB_SUFFIX_TEMP + String.valueOf(companyId);
                break;
            case ip_log:
                tableName = TAB_SUFFIX_IP_LOG + String.valueOf(companyId);
                break;
            case timer:
                tableName = TAB_SUFFIX_TIMER + String.valueOf(companyId);
                break;
            case news:
                tableName = TAB_SUFFIX_NEWS + String.valueOf(companyId);
                break;
            case gold_temp:
                tableName = TAB_SUFFIX_GOLD_TEMP + String.valueOf(companyId);
                break;
            case allot:
                tableName = TAB_SUFFIX_ALLOT_LOG + String.valueOf(companyId);
                break;
            case line_log:
                tableName = TAB_SUFFIX_LINE_LOG + String.valueOf(companyId);
                break;
            case accept_log:
                tableName = TAB_SUFFIX_ACCEPT_LOG + String.valueOf(companyId);
                break;
            case remark:
                tableName = TAB_SUFFIX_REMARK + String.valueOf(companyId);
                break;
            case syslog:
                tableName = TAB_SYSLOG_REMARK + String.valueOf(companyId);
                break;
            case online_time:
                tableName = TAB_ONLINE_TIME + String.valueOf(companyId);
                break;
            default:
                break;
        }
        return tableName += " ";
    }

    /**
     * 获取季度分表表名
     *
     * @param tableEnum
     * @param companyId
     * @param suffix
     * @return
     */
    public static String getTableNamePlus(TableEnum tableEnum, int companyId, String suffix) {
        return (" " + StringUtil.nullToStrTrim(getTable(tableEnum, companyId)) + StringUtil.nullToStrTrim(suffix)
                + " ");
    }

    /**
     * 获取季度分表当前季度表名
     *
     * @param tableEnum
     * @param companyId
     * @return
     * @throws RException
     */
    public static String getTableNamePlus(TableEnum tableEnum, int companyId) throws RException {

        return getTableNamePlus(tableEnum, companyId, getCurrentSuffix());
    }

    /**
     * 根据查询的开始时间和结束时间获取要查询的表后缀名集合
     *
     * @param start
     * @param end
     * @return
     * @throws RException
     */
    public static List<String> getInfoTableSuffix(String start, String end) throws RException {
        if (StringUtil.isEmpty(start) || StringUtil.isEmpty(end)) {
            return null;
        }
        int startYear = getYearFromData(start);
        int endYear = getYearFromData(end);
        int startQuarter = getQuarterFromData(start);
        int endQuarter = getQuarterFromData(end);

        List<String> tabList = null;
        if (startYear >= endYear) {
            tabList = getTableSuffixSameYear(startYear, startQuarter, endQuarter);
        } else {
            tabList = getTableSuffixDiffYear(startYear, endYear, startQuarter, endQuarter);
        }
        Collections.reverse(tabList);
        return tabList;
    }

    /**
     * 获取同年度下表后缀
     *
     * @param year
     * @param startQuarter
     * @param endQuarter
     * @return
     */
    public static List<String> getTableSuffixSameYear(int year, int startQuarter, int endQuarter) {
        List<String> tabNameList = new LinkedList<String>();
        String tabName = "";
        // 同年度查询
        if (startQuarter >= endQuarter) {
            // 同季度查询
            tabName = "";
            tabName += "_";
            tabName += year;
            tabName += "_0";
            tabName += startQuarter;
            tabNameList.add(tabName);
        } else {
            tabName = "";
            tabName += "_";
            tabName += year;
            tabName += "_0";
            tabName += startQuarter;
            tabNameList.add(tabName);
            while (startQuarter < endQuarter) {
                startQuarter++;
                tabName = "";
                tabName += "_";
                tabName += year;
                tabName += "_0";
                tabName += startQuarter;
                tabNameList.add(tabName);
            }
        }

        return tabNameList;
    }

    /**
     * 获取不同年份下的表后缀集合
     *
     * @param starYear
     * @param endYear
     * @param startQuarter
     * @param endQuarter
     * @return
     */
    public static List<String> getTableSuffixDiffYear(int startYear, int endYear, int startQuarter, int endQuarter) {
        List<String> tabNameList = getTableSuffixSameYear(startYear, startQuarter, 4);
        while (startYear < endYear) {
            startYear++;
            if (startYear == endYear) {
                tabNameList.addAll(getTableSuffixSameYear(startYear, 1, endQuarter));
            } else {
                tabNameList.addAll(getTableSuffixSameYear(startYear, 1, 4));
            }
        }
        return tabNameList;
    }

    /**
     * 获取目标时间所在年份
     *
     * @param time
     * @return
     * @throws RException
     */
    public static int getYearFromData(String date) throws RException {
        if (StringUtil.isEmpty(date) || date.length() < 4) {
            throw new RException(ExceptionEnum.DB_SPLIT_ERROR);
        }
        date = date.substring(0, 4);

        return Integer.valueOf(date);
    }

    /**
     * 根据目标时间获取季度
     *
     * @param date
     * @return
     * @throws RException
     */
    public static int getQuarterFromData(String date) throws RException {
        if (StringUtil.isEmpty(date) || date.length() < 7) {
            throw new RException(ExceptionEnum.DB_SPLIT_ERROR);
        }
        date = date.substring(5, 7);
        int month = Integer.valueOf(date);
        int quarter = 0;
        if (month < 4) {
            quarter = 1;
        } else if (month < 7) {
            quarter = 2;
        } else if (month < 10) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }

    /**
     * 获取当前时间对应的分表后缀
     *
     * @return
     * @throws RException
     */
    public static String getCurrentSuffix() throws RException {
        String str = "_";
        str += getYearFromData(TimeUtil.getSysdate());
        str += "_0";
        str += getQuarterFromData(TimeUtil.getSysdate());
        return str;
    }

    /**
     * 获取全表名后缀
     *
     * @return
     * @throws RException
     */
    public static List<String> getAllTableSuffix() throws RException {
        return getInfoTableSuffix("2017-01-01", TimeUtil.getSysdate());
    }

    public static void main(String[] args) throws RException {
        System.out.println(getTableNamePlus(TableEnum.info, 2222));

    }
}