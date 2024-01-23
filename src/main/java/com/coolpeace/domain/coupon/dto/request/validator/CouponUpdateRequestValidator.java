package com.coolpeace.domain.coupon.dto.request.validator;

import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CouponUpdateRequestValidator extends CouponRequestValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CouponUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CouponUpdateRequest request = (CouponUpdateRequest) target;

        validateRegisterRooms(errors, request.registerAllRoom());
        validateDiscountValues(errors, request.discountType(), request.discountFlatValue(), request.discountFlatRate());
    }
}
