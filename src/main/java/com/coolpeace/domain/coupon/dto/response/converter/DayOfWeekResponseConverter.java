package com.coolpeace.domain.coupon.dto.response.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class DayOfWeekResponseConverter implements Converter<DayOfWeek, String> {
    @Override
    public String convert(DayOfWeek source) {
        return source.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }
}
