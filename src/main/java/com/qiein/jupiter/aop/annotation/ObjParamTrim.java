package com.qiein.jupiter.aop.annotation;

import java.lang.annotation.*;

/**
 * 对象参数String trim
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObjParamTrim {
    String value() default "";
}
