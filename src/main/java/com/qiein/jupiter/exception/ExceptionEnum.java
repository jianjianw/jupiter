package com.qiein.jupiter.exception;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1, "未知错误"),
    USER_NOT_FIND(-100001, "用户不存在"),
    USER_IS_DEL(-100001, "用户已离职"),
    USER_IS_LOCK(-100002, "用户已锁定"),
    PASSWORD_ERROR(-100003, "密码错误"),;

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
