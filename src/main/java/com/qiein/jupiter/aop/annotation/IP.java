package com.qiein.jupiter.aop.annotation;

import com.qiein.jupiter.aop.validate.IPValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ip地址校验的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {IPValidator.class})
public @interface IP {
    String field() default "";

    String message() default "ip地址格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
