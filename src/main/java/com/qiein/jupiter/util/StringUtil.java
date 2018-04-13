package com.qiein.jupiter.util;

/**
 * 字符工具类
 */
public class StringUtil {

    /**
     * 将字符串去除空格，如果是空则输出""
     *
     * @param str
     * @return
     */
    public static String nullToStrTrim(String str) {
        if (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    /**
     * 判断两个字符串是否相等,忽略大小写并且取出首尾空格
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean ignoreCaseEqual(String str1, String str2) {
        return nullToStrTrim(str1).equalsIgnoreCase(nullToStrTrim(str2));
    }

    /**
     * 判断一个字符是否是null 或者空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullStr(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * 判断一个字符是否是null 或者空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNotNullStr(String str) {
        return str != null && !str.trim().equals("");
    }

}
