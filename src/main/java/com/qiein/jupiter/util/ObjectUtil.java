package com.qiein.jupiter.util;

import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.ObjectConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 对象工具类
 */
public class ObjectUtil {

    public static void reflect(Object obj) throws Exception {
        Class cls = obj.getClass();
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
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Type genericType = f.getGenericType();
            //如果为空，或者不是String 类型，跳出
            if (genericType == null || !genericType.toString().equals(ObjectConstants.CLASS_TYPE_STRING)) {
                continue;
            }
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
