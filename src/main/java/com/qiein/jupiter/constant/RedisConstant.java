package com.qiein.jupiter.constant;

/**
 * redis常量
 */
public class RedisConstant {

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

    /**
     * 微信accesstoken
     */
    public static String getWeChatKey(String key) {
        return "wechat-key" + splitChar + key;
    }

    /**
     * 钉钉accesstoken
     */
    public static String getDingKey(String key) {
        return "ding-key" + splitChar + key;
    }
}
