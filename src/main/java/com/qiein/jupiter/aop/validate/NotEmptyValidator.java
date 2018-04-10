package com.qiein.jupiter.aop.validate;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.util.StringUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 空值及空字符串验证
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
    @Override
    public void initialize(NotEmpty notNull) {

    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtil.isNullStr(str);
    }
}
