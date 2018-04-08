package com.qiein.jupiter.util;

import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * @创建时间： 2016年1月20日 @相关参数： @param email
     * email地址，格式：qq@zuidaima.com，LinkinPark@xxx.com.cn，
     * xxx代表邮件服务商 @相关参数： @return 验证成功返回true，验证失败返回false @功能描述： 验证Email
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * @创建时间： 2016年1月20日 @相关参数： @param idCard
     * 居民身份证号码15位或18位，最后一位可能是数字或字母 @相关参数： @return 验证成功返回true，验证失败返回false @功能描述：
     * 验证身份证号码
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * @创建时间： 2016年1月20日 @相关参数： @param mobile 移动、联通、电信运营商的号码段
     * <p>
     * 移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     * 、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     * </p>
     * <p>
     * 联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     * </p>
     * <p>
     * 电信的号段：133、153、180（未启用）、189
     * </p>
     * @相关参数： @return 验证成功返回true，验证失败返回false @功能描述：
     * 验证手机号码。支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3456789]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    public static boolean checkNickname(String name) {
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, name);
    }

    /**
     * @创建时间： 2016年1月20日 @相关参数： @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） +
     * 电话号码，如：+8602085588447
     * <p>
     * <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     * 数字之后是空格分隔的国家（地区）代码。
     * </p>
     * <p>
     * <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。
     * </p>
     * <p>
     * <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
     * </p>
     * @相关参数： @return 验证成功返回true，验证失败返回false @功能描述： 验证固定电话号码
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

}
