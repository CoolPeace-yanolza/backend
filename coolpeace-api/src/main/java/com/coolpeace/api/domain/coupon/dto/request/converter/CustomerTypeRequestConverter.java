package com.coolpeace.api.domain.coupon.dto.request.converter;

import com.coolpeace.core.domain.coupon.entity.type.CustomerType;
import org.springframework.core.convert.converter.Converter;

public class CustomerTypeRequestConverter implements Converter<String, CustomerType> {
    @Override
    public CustomerType convert(String customerTypeString) {
        return CustomerType.valueOf(customerTypeString.toUpperCase());
    }
}
