package com.qiein.jupiter.constant;

/**
 * 常用数字常量类
 */
public class NumberConstant {

    /**
     * 默认0
     */
    public final static int DEFAULT_ZERO = 0;
    /**
     * 默认成功返回值
     */
    public final static int DEFAULT_SUCCESS_CODE = 100000;
    /**
     * 登录错误时，增加
     */
    public final static int LOGIN_ERROR_ADD_NUM = 1;
    /**
     * 用户登录错误多少次出现验证码
     */
    public final static int ALLOW_USER_LOGIN_ERR_NUM = 3;

    /**
     * 用户登录错误次数过期时间，1小时
     */
    public final static int LOGIN_ERROR_NUM_EXPIRE_TIME = 1;

    /**
     * token过期时间，12小时
     */
    public final static int DEFAULT_EXPIRE_TIME = 12;
}
