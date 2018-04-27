package com.qiein.jupiter.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.qiein.jupiter.constant.ObjectTypeConstant;

/**
 * 对象工具类
 */
public class ObjectUtil {

	@SuppressWarnings("unused")
	private static void reflect(Object obj) throws Exception {
		Class<?> cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			System.out.println(f.getGenericType());
			System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(obj));
		}
	}

	/**
	 * 把对象中属于string类型的参数 trim
	 */
	public static void objectStrParamTrim(Object obj) {
		Class<?> cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		// 遍历对象
		for (Field f : fields) {
			f.setAccessible(true);
			Type genericType = f.getGenericType();
			// 如果为空，或者不是String 类型，跳出
			if (genericType == null || !genericType.toString().equals(ObjectTypeConstant.CLASS_TYPE_STRING)) {
				continue;
			}
			// 获取对象属性，如果不为空，则trim
			try {
				Object paramObj = f.get(obj);
				if (paramObj != null) {
					f.set(obj, paramObj.toString().trim());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
