package com.qiein.jupiter.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密类
 */
public class MD5Util {

    private final static String SALT = "qieinmW61rAUV5Ot3q1zOin";

    /**
     * md5加密
     *
     * @param str 要加密的字符串
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 循环加密md5
     *
     * @param loop 循环次数
     * @param str  要加密的字符串
     * @return
     */
    public String getMD5ByLoop(int loop, String str) {
        while (loop > 0) {
            str = getMD5(str);
            loop -= 1;
        }
        return str;
    }

    /**
     * 对字符串返回加盐后的md5
     */
    public static String getSaltMd5(String str) {
        str = str + SALT;
        return getMD5(str);
    }

}
