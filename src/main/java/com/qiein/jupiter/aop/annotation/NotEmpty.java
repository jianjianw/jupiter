package com.qiein.jupiter.aop.annotation;


import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.aop.validate.NotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {NotEmptyValidator.class})
public @interface NotEmpty {

    String field() default "";

    String message() default "字段不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
