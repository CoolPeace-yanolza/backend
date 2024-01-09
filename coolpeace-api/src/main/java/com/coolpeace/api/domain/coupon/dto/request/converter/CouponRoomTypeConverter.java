package com.coolpeace.api.domain.coupon.dto.request.converter;

import com.coolpeace.api.domain.coupon.entity.type.CouponRoomType;
import org.springframework.core.convert.converter.Converter;

public class CouponRoomTypeConverter implements Converter<String, CouponRoomType> {
    @Override
    public CouponRoomType convert(String couponRoomTypeString) {
        return CouponRoomType.valueOf(couponRoomTypeString.toUpperCase());
    }
}
