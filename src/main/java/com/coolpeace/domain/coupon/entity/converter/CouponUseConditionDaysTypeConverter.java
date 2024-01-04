package com.coolpeace.domain.coupon.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Converter
public class CouponUseConditionDaysTypeConverter implements AttributeConverter<List<DayOfWeek>, Byte> {
    @Override
    public Byte convertToDatabaseColumn(List<DayOfWeek> CouponUseConditionDays) {
        return CouponUseConditionDays.stream()
                .reduce(0,
                        (days, day) -> days | (1 << (day.getValue() - 1)),
                        (a, b) -> a | b)
                .byteValue();
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(Byte daysByte) {
        return Arrays.stream(DayOfWeek.values())
                .filter(day -> (daysByte & (1 << (day.getValue() - 1))) != 0)
                .toList();
    }
}
