package com.qiein.jupiter.exception;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 */
@Order(-1)
@RestControllerAdvice
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(RRException.class)
    public ResultInfo handleRRException(RRException e) {
        ResultInfo r = new ResultInfo();
        r.setCode(e.getCode());
        r.setMsg(e.getMessage());
        return r;
    }

    /**
     * 全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
    }
}
