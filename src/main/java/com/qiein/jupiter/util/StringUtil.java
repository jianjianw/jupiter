package com.qiein.jupiter.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.qiein.jupiter.constant.CommonConstant;

/**
 * 字符工具类
 */
public class StringUtil {

    public static final double MONEY_MIN = 0.01D;
    public static final double MONET_MAX = 1000000000.00D;

    /**
     * 未知
     */
    public static final String UNKNOWN = "unknown";
    public static final String STR_NULL = "null";
    public static final String STR_UNDEFINED = "undefined";

    private static SecureRandom secureRand = null;
    private static Random rand = null;

    private static String localhost = "";

    static {
        secureRand = new SecureRandom();
        rand = new Random(secureRand.nextLong());
    }

    public static boolean isEmpty(String str) {

        return ((str == null) || (str.trim().length() == 0));
    }

    public static boolean isAllEmpty(String... strs) {
        for (String str : strs) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为半角字符集
     *
     * @param str
     * @return
     */
    public static boolean isHalfAngle(String str) {

        str = nullToStrTrim(str);
        return str.length() == getWordLength(str);
    }

    public static boolean isInvalid(String str) {
        return (isEmpty(str) || UNKNOWN.equalsIgnoreCase(str.trim()) || STR_NULL.equalsIgnoreCase(str.trim())
                || STR_UNDEFINED.equalsIgnoreCase(str.trim()));
    }

    public static boolean haveEmpty(String... strs) {
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {

        return ((str != null) && (str.trim().length() > 0));
    }

    public static boolean haveNotEmpty(String... strs) {
        for (String str : strs) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean haveEmpty(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String str) {
        if (isEmpty(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return false;
    }

    public static String nullToStr(String str) {

        return str == null ? "" : str;
    }

    public static String nullToStrTrim(String str) {

        return str == null ? "" : str.trim();
    }

    public static int nullToIntZero(String str) {

        str = isEmpty(str) ? "0" : str;
        return Integer.valueOf(str.trim(), 10);
    }

    public static double nullToDoubleZero(String str) {

        str = isEmpty(str) ? "0.00" : str;
        return Double.valueOf(str.trim());
    }

    public static long nullToLongZero(String str) {

        str = isEmpty(str) ? "0" : str;
        return Long.valueOf(str.trim(), 10);
    }

    public static boolean nullToBoolean(String str) {

        return isEmpty(str) ? false : Boolean.valueOf(str.trim());
    }

    public static String nullToUnKnown(String str) {

        return isEmpty(str) ? UNKNOWN : str.trim();
    }

    public static String encodeHTML(String str) {
        if (isEmpty(str)) {
            return "";
        }
        char[] content = new char[str.length()];
        str.getChars(0, str.length(), content, 0);
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#039;");
                    break;
                default:
                    sb.append(content[i]);
                    break;
            }
        }

        return sb.toString();
    }

    /**
     * 获取字符串的绝对长度，一个中文字符长度为2
     *
     * @param str
     * @return
     */
    public static int getWordLength(String str) {

        return nullToStr(str).replaceAll("[^\\x00-\\xff]", "**").length();
    }

    public static String getUUID() {

        return UUID.randomUUID().toString();
    }

    /**
     * 根据参数给定范围获取整数随机数
     *
     * @param accuracy
     * @return
     */
    public static int getRandom(int accuracy) {

        return (int) (Math.random() * accuracy);
    }

    public static String getRawRandom() {

        String str = MD5Util
                .getMD5(getUUID() + System.currentTimeMillis() + getRandom(999999999) + rand.nextLong() + localhost);
        return str.toLowerCase();
    }

    public static String getRawRandom(String string) {

        String str = MD5Util.getMD5(
                getUUID() + System.currentTimeMillis() + getRandom(999999999) + rand.nextLong() + localhost + string);
        return str.toLowerCase();
    }

    public static String getRandom() {

        String str = MD5Util.getMD5(getUUID() + System.currentTimeMillis() + getRandom(999999999) + rand.nextLong());
        return str.toLowerCase();
    }

    public static String getRandom(String string) {

        String str = MD5Util
                .getMD5(getUUID() + System.currentTimeMillis() + getRandom(999999999) + rand.nextLong() + string);
        return str.toLowerCase();
    }

    /**
     * 去除字符串首位和末尾的指定字符
     *
     * @param str
     * @param charStr
     * @return
     */
    public static String subStartAndEndChar(String str, String charStr) {

        if (isEmpty(str) || isEmpty(charStr)) {
            return str;
        }

        while (str.startsWith(charStr)) {
            str = str.substring(1);
        }
        while (str.endsWith(charStr)) {
            str = str.substring(0, str.length() - 1);
        }

        return nullToStrTrim(str);
    }

    public static String emptyToNull(String str) {

        if (isEmpty(str)) {
            return null;
        }

        return str;
    }

    /**
     * 替换掉字符串内的HTML元素
     *
     * @param str
     * @return
     */
    public static String replaceAllHTML(String str, String rep) {

        if (isEmpty(str)) {
            return "";
        }
        str = replaceHTMLStyleScript(str);

        str = str.replaceAll("&nbsp;", " ");

        return str.replaceAll("<[^>]*>", nullToStrTrim(rep));
    }

    /**
     * 替换掉字符串内的HTML元素
     *
     * @param str
     * @return
     */
    public static String replaceAllHTML(String str) {

        return replaceAllHTML(str, "");
    }

    /**
     * 删除字符串内的style元素和script元素
     *
     * @return
     */
    public static String replaceHTMLStyleScript(String str) {

        if (isEmpty(str)) {
            return "";
        }

        StringBuffer sb = new StringBuffer(str);

        int start = sb.indexOf("<style");
        int end = sb.indexOf("</style>");

        while (start != -1 && end != -1) {
            sb.delete(start, end + 8);
            start = sb.indexOf("<style");
            end = sb.indexOf("</style>");
        }

        start = sb.indexOf("<script");
        end = sb.indexOf("</script>");

        while (start != -1 && end != -1) {
            sb.delete(start, end + 9);
            start = sb.indexOf("<script");
            end = sb.indexOf("</script>");
        }

        str = sb.toString();

        str = str.replace("<style>", "");
        str = str.replace("</style>", "");
        str = str.replace("<script>", "");
        str = str.replace("</script>", "");

        return str;
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static String subStr(String str, int length) {

        if (isEmpty(str) || str.length() < length) {
            return str;
        }

        if (length <= 0) {
            return "";
        }

        return str.substring(0, length);
    }

    /**
     * 获取富文本里的图片地址
     *
     * @param str
     * @return
     */
    public static List<String> getImgLists(String str) {
        List<String> urlList = new LinkedList<String>();
        if (isEmpty(str)) {
            return null;
        }
        String img = "";
        while (true) {
            // 1.如果是截图
            int i = str.indexOf("src=\"http://qieinoa");
            int b = str.indexOf("png");
            if (i > 0 && b > 0) {
                img = str.substring(i + 5, b + 3);
                urlList.add(img);
                str = str.replace("src=\"" + img + "\"", "");
            }
            // 2.如果是黏贴图
            i = str.indexOf("src=\"http");
            if (i > 0) {
                str = str.substring(i + 5, str.length());
                b = str.indexOf(">");
                if (b > 0) {
                    img = str.substring(0, b - 1);
                    urlList.add(img);
                    str = str.replace(img + "\"", "");
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return urlList;
    }

    /**
     * 去掉富文本里的图片地址
     *
     * @param str
     * @return
     */
    public static String removeImgLists(String str) {
        if (isEmpty(str)) {
            return null;
        }
        while (true) {
            int start = str.indexOf("<img");
            int end = str.indexOf(">", start);
            if (start > 0 && end > 0) {
                String img = str.substring(start, end + 1);
                str = str.replace(img, "");
            } else {
                break;
            }
        }
        return str;
    }

    public static String encode(String str) {

        return encode(str, CommonConstant.CHARSETNAME_DEFAULT);
    }

    public static String encode(String str, String enc) {

        String strEncode = "";

        try {
            if (str != null)
                strEncode = URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException e) {
        }

        return strEncode;
    }

    public static int getRealLength(String str) {

        return getRealLength(str, CommonConstant.CHARSETNAME_DEFAULT);
    }

    public static int getRealLength(String str, String charsetName) {

        str = nullToStrTrim(str);

        if (isEmpty(str)) {
            return 0;
        }

        try {
            return str.getBytes(charsetName).length;
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }

    public static String decode(String str) {

        return decode(str, CommonConstant.CHARSETNAME_DEFAULT);
    }

    public static String decode(String str, String enc) {

        String strDecode = "";

        try {
            if (str != null)
                strDecode = URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
        }

        return strDecode;
    }

    public static boolean ignoreCaseEqual(String token1, String token2) {
        if (haveEmpty(token1, token2)) {
            return false;
        }
        return nullToStrTrim(token1).toLowerCase().equals(nullToStrTrim(token2).toLowerCase());
    }

    public static String byte2hex(byte[] b) {

        String str = "";
        String stmp = "";

        int length = b.length;

        for (int n = 0; n < length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                str += "0";
            }
            str += stmp;
        }

        return str.toLowerCase();
    }

    /**
     * 转换为下划线
     *
     * @param camelCaseName
     * @return
     */
    public static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 转换为驼峰
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}