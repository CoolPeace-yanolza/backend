package com.coolpeace.global.common.validator;

import com.coolpeace.global.common.ValuedEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValuedEnumValidator implements ConstraintValidator<ValidEnum, String> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        ValuedEnum[] enumValues = (ValuedEnum[]) this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (ValuedEnum enumValue : enumValues) {
                if (enumValue.getValue().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}