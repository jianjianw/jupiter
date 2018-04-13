package com.qiein.jupiter.util;

import java.util.List;

public class ListUtil {

    /**
     * 判断一个集合是否为空
     */
    public static boolean isNullList(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断一个集合不为空
     */
    public static boolean isNotNullList(List list) {
        return list != null && !list.isEmpty();
    }

}
