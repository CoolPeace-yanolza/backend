package com.coolpeace.api.domain.coupon.dto.request.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.DayOfWeek;

public class DayOfWeekRequestConverter implements Converter<String, DayOfWeek> {
    @Override
    public DayOfWeek convert(String discountTypeString) {
        return DayOfWeek.valueOf(discountTypeString.toUpperCase());
    }
}
