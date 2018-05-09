package com.qiein.jupiter.constant;

/**
 * 常用String常量
 */
public class CommonConstant {

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
	public static final int DEFAULT_OVERTIME = 120;

	/**
	 * 默认客服领取客资时间间隔
	 */
	public static final int DEFAULT_INTERVAL = 180;

	/**
	 * 分配客资时依据历史分配情况的时间范围-分钟
	 */
	public static final int ALLOT_RANGE_DEFAULT = 60;

	/**
	 * 非全量分配情况时对应的时间扩展增额-分钟
	 */
	public static final int ALLOT_RANGE_INTERVAL = 120;

	/**
	 * 分配客资检索时的最大时间范围-分钟
	 */
	public static final int ALLOT_RANGE_MAX = 540;
}