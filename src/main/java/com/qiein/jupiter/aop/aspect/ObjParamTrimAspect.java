package com.qiein.jupiter.aop.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 对象参数trim 切面类
 */
@Aspect
@Component
public class ObjParamTrimAspect {

    @Pointcut("@annotation(com.qiein.jupiter.aop.annotation.ObjParamTrim)")
    public void objPointCut() {

    }

    @Before("objPointCut()")
    public void objTrim(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();
    }
}
