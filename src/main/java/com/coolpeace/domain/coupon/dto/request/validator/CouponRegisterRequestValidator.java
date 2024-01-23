package com.coolpeace.domain.coupon.dto.request.validator;

import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CouponRegisterRequestValidator extends CouponRequestValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CouponRegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CouponRegisterRequest request = (CouponRegisterRequest) target;

        validateRegisterRooms(errors, request.registerAllRoom());
        validateDiscountValues(errors, request.discountType(), request.discountFlatValue(), request.discountFlatRate());
    }
}
