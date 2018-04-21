package com.qiein.jupiter.aop.aspect.annotation;


import java.lang.annotation.*;

/**
 * 登录日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    String value() default "";
}
