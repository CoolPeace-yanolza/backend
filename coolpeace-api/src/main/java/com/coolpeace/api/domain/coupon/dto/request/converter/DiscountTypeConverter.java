package com.coolpeace.api.domain.coupon.dto.request.converter;

import com.coolpeace.api.domain.coupon.entity.type.DiscountType;
import org.springframework.core.convert.converter.Converter;

public class DiscountTypeConverter implements Converter<String, DiscountType> {
    @Override
    public DiscountType convert(String discountTypeString) {
        return DiscountType.valueOf(discountTypeString.toUpperCase());
    }
}
