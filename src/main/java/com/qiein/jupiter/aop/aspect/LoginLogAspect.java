package com.qiein.jupiter.aop.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 系统日志切面
 */
@Aspect
@Component
public class LoginLogAspect {

    @Pointcut("@annotation(com.qiein.jupiter.aop.annotation.LoginLog)")
    public void loginLogPointCut() {

    }

    @Before("loginLogPointCut()")
    public void objTrim(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();
    }
}
