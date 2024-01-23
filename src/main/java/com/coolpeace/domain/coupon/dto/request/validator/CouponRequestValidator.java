package com.coolpeace.domain.coupon.dto.request.validator;

import com.coolpeace.domain.coupon.entity.type.DiscountType;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public abstract class CouponRequestValidator implements Validator {

    // 방 등록 유효성 검사
    protected static void validateRegisterRooms(Errors errors, boolean registerAllRoom) {
        if (!registerAllRoom) {
            ValidationUtils.rejectIfEmpty(errors,
                    "registerRooms",
                    "registerRooms.empty",
                    "등록할 객실을 선택해주세요.");
        }
    }

    // 할인 유형 유효성 검사
    protected static void validateDiscountValues(Errors errors,
                                                 String requestedDiscountTypeStr,
                                                 Integer requestedDiscountFlatValue,
                                                 Integer requestedDiscountFlatRate) {
        if (requestedDiscountTypeStr.equals(DiscountType.FIXED_PRICE.getValue())) {
            if (requestedDiscountFlatValue == null || requestedDiscountFlatValue <= 0) {
                errors.reject("discountFlatValue.empty", "정액 할인의 경우 할인 금액를 입력해야 합니다.");
            }
        } else {
            if (requestedDiscountFlatRate == null || requestedDiscountFlatRate <= 0) {
                errors.reject("discountFlatValue.empty", "정률 할인의 경우 할인 비율을 입력해야 합니다.");
            }
        }
    }
}
