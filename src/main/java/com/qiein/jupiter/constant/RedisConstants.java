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
     * 用户验证码
     *
     * @param userName
     * @return
     */
    public static String getVerifyCodeKey(String userName) {
        return "verify_code" + splitChar + userName;
    }

    /**
     * 获取用户登录错误次数的key
     *
     * @param userName
     * @return
     */
    public static String getUserLoginErrNumKey(String userName) {
        return "user_login_err_num" + splitChar + userName;
    }

    /**
     * 获取用户token key
     *
     * @param uid 用户id
     * @param cid 公司id
     * @return
     */
    public static String getStaffKey(int uid, int cid) {
        return "staff" + splitChar + uid + splitChar + cid;
    }
}
