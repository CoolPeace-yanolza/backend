package com.coolpeace.domain.coupon.dto.response.converter;

import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import org.springframework.core.convert.converter.Converter;

public class CouponStatusTypeResponseConverter implements Converter<CouponStatusType, String> {
    @Override
    public String convert(CouponStatusType source) {
        return source.getValue();
    }
}
