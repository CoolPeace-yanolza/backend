package com.coolpeace.domain.coupon.dto.response.converter;

import com.coolpeace.domain.coupon.entity.type.CustomerType;
import org.springframework.core.convert.converter.Converter;

public class CustomerTypeResponseConverter implements Converter<CustomerType, String> {
    @Override
    public String convert(CustomerType source) {
        return source.getValue();
    }
}
