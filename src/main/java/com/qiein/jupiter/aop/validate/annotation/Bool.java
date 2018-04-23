package com.qiein.jupiter.aop.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.qiein.jupiter.aop.validate.BooleanValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER })
@Constraint(validatedBy = { BooleanValidator.class })
public @interface Bool {
	String field() default "";

	String message() default "显示不能为空";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}