package com.coolpeace.domain.coupon.dto.request.validator;

import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CouponRegisterRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CouponRegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CouponRegisterRequest request = (CouponRegisterRequest) target;

        // 방 등록
        if (!request.registerAllRoom()) {
            ValidationUtils.rejectIfEmpty(errors,
                    "registerRooms",
                    "registerRooms.empty",
                    "등록할 객실을 선택해주세요.");
        }
    }
}
