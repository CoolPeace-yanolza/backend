package com.coolpeace.domain.coupon.dto.response.converter;

import com.coolpeace.domain.coupon.entity.type.DiscountType;
import org.springframework.core.convert.converter.Converter;

public class DiscountTypeResponseConverter implements Converter<DiscountType, String> {
    @Override
    public String convert(DiscountType source) {
        return source.getValue();
    }
}
