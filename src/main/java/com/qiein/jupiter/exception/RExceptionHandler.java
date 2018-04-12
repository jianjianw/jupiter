package com.qiein.jupiter.exception;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.apache.ibatis.reflection.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

/**
 * 全局方法异常处理器
 */
@Order(-1)
@RestControllerAdvice
public class RExceptionHandler {
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

    /**
     * 参数校验失败的异常
     *
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder stringBuffer = new StringBuilder();
        for (ObjectError allError : allErrors) {
            stringBuffer.append(allError.getDefaultMessage()).append(";");

        }
        return ResultInfoUtil.error(-100000, stringBuffer.toString());
    }

    /**
     * http body参数无法读取转换
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultInfo handleHttpMessageNotReadableException() {
        return ResultInfoUtil.error(ExceptionEnum.HTTP_BODY_NOT_READABLE);
    }

    /**
     * mysql语法错误
     *
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResultInfo handleBadSqlGrammarException() {
        return ResultInfoUtil.error(ExceptionEnum.MYSQL_SQL_GRAMMAR_ERROR);
    }

}