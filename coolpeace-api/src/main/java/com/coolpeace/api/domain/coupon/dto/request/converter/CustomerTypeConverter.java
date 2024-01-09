package com.coolpeace.api.domain.coupon.dto.request.converter;

import com.coolpeace.api.domain.coupon.entity.type.CustomerType;
import org.springframework.core.convert.converter.Converter;

public class CustomerTypeConverter implements Converter<String, CustomerType> {
    @Override
    public CustomerType convert(String customerTypeString) {
        return CustomerType.valueOf(customerTypeString.toUpperCase());
    }
}
