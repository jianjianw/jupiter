package com.qiein.jupiter.aop.annotation;


import com.qiein.jupiter.aop.validate.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {IdValidator.class})
public @interface Id {
    String field() default "";

    String message() default "Id不能为空或者0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}