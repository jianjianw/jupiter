package com.qiein.jupiter.util;

/**
 * 字符工具类
 */
public class StringUtil {

    public static String nullToStrTrim(String str) {

        if (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim())) {
            str = "";
        }

        return str.trim();
    }

}
