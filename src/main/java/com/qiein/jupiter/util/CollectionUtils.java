package com.qiein.jupiter.util;

import java.util.Collection;
import java.util.Map;

/**
 * 集合操作工具类
 * 
 * @author JingChenglong 2018/04/27 11:11
 *
 */
public class CollectionUtils {

	public static boolean isEmpty(Collection<?> collection) {

		return (collection == null || collection.size() == 0);
	}

	public static boolean isNotEmpty(Collection<?> collection) {

		return (collection != null && collection.size() > 0);
	}

	public static boolean isEmpty(Map<?, ?> map) {

		return (map == null || map.size() == 0);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {

		return (map != null && map.size() > 0);
	}
}