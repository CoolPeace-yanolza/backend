package com.coolpeace.domain.coupon.dto.response.converter;

import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import org.springframework.core.convert.converter.Converter;

public class CouponRoomTypeResponseConverter implements Converter<CouponRoomType, String> {
    @Override
    public String convert(CouponRoomType source) {
        return source.getValue();
    }
}
