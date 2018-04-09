package com.qiein.jupiter.constant;

/**
 * 常用常量
 */
public class CommonConstants {
    /**
     * 用户验证码
     */
    public final static String USER_VERIFY_CODE = "userVerifyCode";

    /**
     * 用户登录错误次数
     */
    public final static String USER_LOGIN_ERR_NUM = "userLoginErrNum";

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
    public final static int TOKEN_EXPIRE_TIME = 12;

    /**
     * jwt body
     */
    public final static String JWT_BODY = "jwtBody";

    /**
     * 保存成功
     */
    public final static String SAVE_SUCCESS = "保存成功";
    /**
     * 开发环境
     */
    public final static String DEV = "dev";

    /**
     * 验证参数。包含token uid cid
     */
    public final static String VERIFY_PARAM = "verifyParam";

    /**
     * token
     */
    public final static String TOKEN = "token";

    /**
     * uid
     */
    public final static String UID = "uid";

    /**
     * cid
     */
    public final static String CID = "cid";

    /**
     * 成功
     */
    public final static String SUCCESS = "成功";

    /**
     * 失败
     */
    public final static String FAIL = "失败";

    /**
     * 错误
     */
    public final static String ERROR = "错误";
}
