package com.qiein.jupiter.aop.validate;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.util.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 空值及空字符串验证
 */
public class NotEmptyStrValidator implements ConstraintValidator<NotEmptyStr, String> {
    @Override
    public void initialize(NotEmptyStr notNull) {

    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtil. isEmpty(str);
    }
}
