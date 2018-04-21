package com.qiein.jupiter.aop.validate.annotation;


import com.qiein.jupiter.aop.validate.NotEmptyStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {NotEmptyStrValidator.class})
public @interface NotEmptyStr {

    String field() default "";

    String message() default "字段不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
