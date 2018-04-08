package com.qiein.jupiter.exception;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 自定义异常
 */
public class RException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code;

    public RException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.msg = exceptionEnum.getMsg();
        this.code = exceptionEnum.getCode();
    }

    public RException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
