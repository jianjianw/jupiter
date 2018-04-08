package com.qiein.jupiter.exception;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局方法异常处理器
 */
@Order(-1)
@RestControllerAdvice
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(RException.class)
    public ResultInfo handleRRException(RException e) {
        ResultInfo r = new ResultInfo();
        r.setCode(e.getCode());
        r.setMsg(e.getMessage());
        return r;
    }

    /**
     * 请求方法异常
     *
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultInfo handleMethodNotException() {
        return ResultInfoUtil.error(ExceptionEnum.HTTP_METHOD_NOT_SUPPORT);
    }
}
