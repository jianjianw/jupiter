package com.qiein.jupiter.constant;

/**
 * 常用String常量
 */
public class CommonConstant {

    /**
     * 默认企业ID
     */
    public final static int DEFAULT_COMPID = 0;

    /**
     * 参数代表-是
     */
    public final static String FLAG_YES = "1";

    /**
     * 空字符串
     */
    public final static String NULL_STR = "";

    /**
     * 用户验证码
     */
    public final static String USER_VERIFY_CODE = "userVerifyCode";

    /**
     * 用户登录错误次数
     */
    public final static String USER_LOGIN_ERR_NUM = "userLoginErrNum";

    /**
     * 当前登录用户
     */
    public final static String CURRENT_LOGIN_STAFF = "currentLoginStaff";

    /**
     * jwt body
     */
    public final static String JWT_BODY = "jwtBody";

    /**
     * 测试环境
     */
    public final static String TEST = "test";

    /**
     * 开发环境
     */
    public final static String DEV = "dev";

    /**
     * 生产环境
     */
    public final static String PRO = "pro";

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
     * 文件分隔符
     */
    public static final String FILE_SEPARATOR = "/";

    /**
     * 多个参数字符串分隔符
     */
    public static final String STR_SEPARATOR = ",";

    /**
     * 横杠分割
     */
    public static final String ROD_SEPARATOR = "-";

    /*-- 编码方式 --*/
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String ENCODING_ISO88591 = "ISO-8859-1";
    public static final String CHARSETNAME_DEFAULT = ENCODING_UTF8;

    /**
     * Excel导入默认备注
     */
    public static final String EXCEL_DEFAULT_REMARK = "<p>通过excel导入客资</p>";

    /**
     * 富文本前缀
     */
    public static final String RICH_TEXT_PREFIX = "<p>";

    /**
     * 富文本后缀
     */
    public static final String RICH_TEXT_SUFFIX = "</p>";

    /**
     * Excel导入默认拍摄类型
     */
    public static final String EXCEL_DEFAULT_PHOTO_TYPE_NAME = "婚纱照";

    /**
     * 默认领取客资超时时间
     */
    public static final int DEFAULT_OVERTIME = 120 * 60;

    /**
     * 默认客服领取客资时间间隔
     */
    public static final int DEFAULT_INTERVAL = 180 * 60;

    /**
     * 分配客资时依据历史分配情况的时间范围-秒
     */
    public static final int ALLOT_RANGE_DEFAULT = 60 * 60;

    /**
     * 非全量分配情况时对应的时间扩展增额-秒
     */
    public static final int ALLOT_RANGE_INTERVAL = 120 * 60;

    /**
     * 分配客资检索时的最大时间范围-秒
     */
    public static final int ALLOT_RANGE_MAX = 540 * 60;

    /**
     * 默认0
     */
    public final static int DEFAULT_ZERO = 0;
    /**
     * 默认字符串0
     */
    public final static String DEFAULT_STRING_ZERO = "0";
    /**
     * 默认成功返回值
     */
    public final static int DEFAULT_SUCCESS_CODE = 100000;

    /**
     * 默认错误返回值
     */
    public final static int DEFAULT_ERROR_CODE = 999999;
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

    /**
     * 在线时长时间，，默认180秒更新一次
     */
    public final static int DEFAULT_ONLINE_TIME = 120;

    /**
     * request信息
     */
    public final static String REQUEST_INFO = "requestInfo";

    public final static int SYSTEM_OPERA_ID = 0;
    public final static String SYSTEM_OPERA_NAME = "草莓卷机器人";
}