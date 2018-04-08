package com.qiein.jupiter.exception;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1, "未知错误"),
    HTTP_METHOD_NOT_SUPPORT(-2, "不支持的请求方法类型"),
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
    DELETE_FAIL(1000013, "删除失败"),;

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
