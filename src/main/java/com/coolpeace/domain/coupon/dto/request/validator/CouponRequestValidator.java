package com.coolpeace.domain.coupon.dto.request.validator;

import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.global.common.ValuedEnum;
import io.jsonwebtoken.lang.Collections;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

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

    // 객실 유형 유효성 검사
    protected static void validateCouponRoomTypes(Errors errors, List<String> couponRoomTypes) {
        if (Collections.isEmpty(couponRoomTypes)) {
            ValidationUtils.rejectIfEmpty(errors,
                    "couponRoomTypes",
                    "couponRoomTypes.empty",
                    "객실 유형을 선택해주세요.");
        }
        for (String couponRoomTypesStr : couponRoomTypes) {
            try {
                ValuedEnum.of(CouponRoomType.class, couponRoomTypesStr);
            } catch (IllegalArgumentException e) {
                errors.reject("couponRoomTypes.invalid", "올바르지 않은 객실 유형입니다.");
            }
        }
    }

    protected static void validateCouponRoomConditionDays(Errors errors,
                                                  String couponUseConditionDays) {
        if (ValuedEnum.of(DtoCouponUseDaysType.class, couponUseConditionDays).equals(DtoCouponUseDaysType.ONEDAY)) {
            ValidationUtils.rejectIfEmpty(errors,
                    "couponUseConditionDayOfWeek",
                    "couponUseConditionDayOfWeek.empty",
                    "사용 조건의 요일을 선택해야 합니다.");
        }
    }
}
