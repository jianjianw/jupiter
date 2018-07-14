package com.qiein.jupiter.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Class<?> tempClass = obj.getClass();
        List<Field> fieldList = new ArrayList<>();
        //递归获取所有的类
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        // 遍历对象
        for (Field f : fieldList) {
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

    /**
     * 根据变量 名字获取变量属性
     *
     * @param obj
     * @param fieldName
     * @throws Exception
     */
    public static Object getObjField(Object obj, String fieldName) throws Exception {
        Class<?> cls = obj.getClass();
        //设置访问权限
//        field.setAccessible(true);
        Field field = cls.getField(fieldName);
        return field.get(obj);
    }
}
