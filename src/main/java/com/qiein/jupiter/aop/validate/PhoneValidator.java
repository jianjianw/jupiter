package com.qiein.jupiter.aop.validate;

import com.qiein.jupiter.aop.validate.annotation.Phone;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码验证类
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone phone) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return !RegexUtil.checkMobile(StringUtil.nullToStrTrim(phone));
    }
}
