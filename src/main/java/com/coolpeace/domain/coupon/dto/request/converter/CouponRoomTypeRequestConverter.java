package com.coolpeace.domain.coupon.dto.request.converter;

import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import org.springframework.core.convert.converter.Converter;

public class CouponRoomTypeRequestConverter implements Converter<String, CouponRoomType> {
    @Override
    public CouponRoomType convert(String couponRoomTypeString) {
        return CouponRoomType.valueOf(couponRoomTypeString.toUpperCase());
    }
}
