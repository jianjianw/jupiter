package com.qiein.jupiter.exception;

/**
 * 自定义异常
 */
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code;

    public RRException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.msg = exceptionEnum.getMsg();
        this.code = exceptionEnum.getCode();
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
