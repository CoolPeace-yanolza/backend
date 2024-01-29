package com.coolpeace.global.common.validator;

import com.coolpeace.global.common.ValuedEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ValuedEnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;
    private boolean required;

    @Override
    public void initialize(ValidEnum constraintAnnotation)
    {
        this.enumClass = constraintAnnotation.enumClass();
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.hasText(value)) {
            ValuedEnum[] enumValues = (ValuedEnum[]) this.enumClass.getEnumConstants();
            // 값이 있으면 무조건 검사 후 유효하면 통과, 유효하지 않으면 실패
            for (ValuedEnum enumValue : enumValues) {
                if (enumValue.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        // 필수인 경우 실패, 선택인 경우 통과
        return !required;
    }
}