package com.qiein.jupiter.constant;

/**
 * redis常量
 */
public class RedisConstants {

    /**
     * 分隔符
     */
    private final static String splitChar = ":";

    /**
     * 验证码
     */
    private final static String verifyCode = "verify_code";

    /**
     * 用户登录错误次数
     */
    private final static String userLoginErrNum = "user_login_err_num";

    /**
     * 用户token
     */
    private final static String userToken = "user_token";

    /**
     * 用户验证码
     *
     * @param userName
     * @return
     */
    public static String getVerifyCode(String userName) {
        return verifyCode + splitChar + userName;
    }

    /**
     * 获取用户登录错误次数的key
     *
     * @param userName
     * @return
     */
    public static String getUserLoginErrNum(String userName) {
        return userLoginErrNum + splitChar + userName;
    }

    /**
     * 获取用户token key
     *
     * @param uid 用户id
     * @param cid 公司id
     * @return
     */
    public static String getUserToken(int uid, int cid) {
        return userToken + splitChar + uid + splitChar + cid;
    }
}
