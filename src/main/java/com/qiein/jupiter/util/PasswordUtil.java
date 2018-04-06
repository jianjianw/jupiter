package com.qiein.jupiter.util;


import java.util.HashSet;
import java.util.Set;

/**
 * 检查密码工具类
 */
public class PasswordUtil {

    /**
     * 简单密码集合
     */
    private static Set<String> simplePasswordSet;

    static {
        simplePasswordSet = new HashSet<>();
        simplePasswordSet.add(MD5Util.getMD5("000000"));
        simplePasswordSet.add(MD5Util.getMD5("111111"));
        simplePasswordSet.add(MD5Util.getMD5("222222"));
        simplePasswordSet.add(MD5Util.getMD5("333333"));
        simplePasswordSet.add(MD5Util.getMD5("444444"));
        simplePasswordSet.add(MD5Util.getMD5("555555"));
        simplePasswordSet.add(MD5Util.getMD5("666666"));
        simplePasswordSet.add(MD5Util.getMD5("777777"));
        simplePasswordSet.add(MD5Util.getMD5("888888"));
        simplePasswordSet.add(MD5Util.getMD5("999999"));
        simplePasswordSet.add(MD5Util.getMD5("123456"));
        simplePasswordSet.add(MD5Util.getMD5("123123"));
        simplePasswordSet.add(MD5Util.getMD5("012345"));
        simplePasswordSet.add(MD5Util.getMD5("123321"));
        simplePasswordSet.add(MD5Util.getMD5("0123456"));
        simplePasswordSet.add(MD5Util.getMD5("01234567"));
        simplePasswordSet.add(MD5Util.getMD5("012345678"));
        simplePasswordSet.add(MD5Util.getMD5("0123456789"));
        simplePasswordSet.add(MD5Util.getMD5("654321"));
        simplePasswordSet.add(MD5Util.getMD5("54321"));
    }

    /**
     * 是否是简单密码
     *
     * @param password
     * @return
     */
    public static boolean isSimplePassword(String password) {
        //密码长度
        boolean len = password.length() < 4;
        //是否在简单密码set
        boolean inSimpleSet = simplePasswordSet.contains(password);
        return len || inSimpleSet;
    }
}
