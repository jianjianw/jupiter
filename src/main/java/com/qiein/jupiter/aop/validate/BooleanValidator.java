package com.qiein.jupiter.aop.validate;


import com.qiein.jupiter.aop.validate.annotation.Bool;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<Bool, Boolean> {


    @Override
    public void initialize(Bool bool) {

    }

    @Override
    public boolean isValid(Boolean bool, ConstraintValidatorContext constraintValidatorContext) {
        return bool != null;
    }
}
