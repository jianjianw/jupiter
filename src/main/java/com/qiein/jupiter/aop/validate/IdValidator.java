package com.qiein.jupiter.aop.validate;

import com.qiein.jupiter.aop.validate.annotation.Id;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * id验证
 */
public class IdValidator implements ConstraintValidator<Id, Integer> {
    @Override
    public void initialize(Id id) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return !(id == null || id == 0);
    }
}
