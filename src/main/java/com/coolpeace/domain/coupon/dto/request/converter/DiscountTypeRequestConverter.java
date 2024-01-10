package com.coolpeace.domain.coupon.dto.request.converter;

import com.coolpeace.domain.coupon.entity.type.DiscountType;
import org.springframework.core.convert.converter.Converter;

public class DiscountTypeRequestConverter implements Converter<String, DiscountType> {
    @Override
    public DiscountType convert(String discountTypeString) {
        return DiscountType.valueOf(discountTypeString.toUpperCase());
    }
}
