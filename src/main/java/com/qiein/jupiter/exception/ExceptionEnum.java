package com.qiein.jupiter.exception;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1, "未知错误"),
    HTTP_METHOD_NOT_SUPPORT(-2, "不支持的请求方法类型"),
    TOKEN_NULL(-3, "token不存在"),
    TOKEN_INVALID(-4, "token失效"),
    TOKEN_VERIFY_FAIL(-5, "token验证失败"),
    VERIFY_PARAM_INCOMPLETE(-6, "token验证参数不全"),
    HTTP_BODY_NOT_READABLE(-7, "http请求体无法读取"),
    MYSQL_SQL_GRAMMAR_ERROR(-8, "sql语法错误"),
    MYBATIS_REFLECTOR_ERROR(-9,"mybatis反射异常"),
    USER_NOT_FIND(100000, "用户不存在"),
    USER_IS_DEL(100001, "用户已被删除"),
    USER_IS_LOCK(100002, "用户已锁定"),
    PASSWORD_ERROR(100003, "密码错误"),
    USERNAME_NULL(100004, "用户名不能为空"),
    PASSWORD_NULL(100005, "密码不能为空"),
    VERIFY_NULL(100006, "验证码不能为空"),
    PHONE_NULL(100007, "手机号码不能为空"),
    VERIFY_ERROR(100008, "验证码错误"),
    PHONE_FOMAT_ERROR(100009, "手机号码格式错误"),
    USERNAME_OR_PASSWORD_ERROR(1000010, "用户名或密码错误"),
    INSERT_FAIL(1000011, "添加失败"),
    UPDATE_FAIL(1000012, "修改失败"),
    DELETE_FAIL(1000013, "删除失败"),
    NICKNAME_NULL(1000014,"艺名不能为空");

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
