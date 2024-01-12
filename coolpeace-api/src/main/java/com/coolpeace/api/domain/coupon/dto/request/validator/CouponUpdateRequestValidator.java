package com.coolpeace.api.domain.coupon.dto.request.validator;

import com.coolpeace.api.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.core.domain.coupon.entity.type.DiscountType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CouponUpdateRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CouponUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CouponRegisterRequest request = (CouponRegisterRequest) target;

        // 할인 유형
        if (request.discountType().equals(DiscountType.FIXED_PRICE)) {
            if (request.discountValue() < 100) {
                errors.reject("discountValue",
                        "정액 할인일 경우 값을 100 이상으로 설정해야 합니다.");
            }
        } else {
            if (request.discountValue() > 100) {
                errors.reject("discountValue",
                        "정률 할인일 경우 값을 100 이하로 설정해야 합니다.");
            }
        }

        // 특정 객실 등록
        if (!request.registerAllRoom()) {
            if (CollectionUtils.isEmpty(request.registerRooms())) {
                errors.reject("registerRooms.empty",
                        "등록할 객실 리스트가 비어 있습니다.");
            }
        }

    }
}
