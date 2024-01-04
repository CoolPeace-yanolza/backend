package com.coolpeace.domain.coupon.entity.converter;

import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class CouponRoomTypeConverter implements AttributeConverter<List<CouponRoomType>, Byte> {

    @Override
    public Byte convertToDatabaseColumn(List<CouponRoomType> couponRoomTypes) {
        return couponRoomTypes.stream()
                .reduce(0,
                        (values, value) -> values | (1 << value.getIndex()),
                        (a, b) -> a | b)
                .byteValue();
    }

    @Override
    public List<CouponRoomType> convertToEntityAttribute(Byte daysByte) {
        return Arrays.stream(CouponRoomType.values())
                .filter(day -> (daysByte & (1 << day.getIndex())) != 0)
                .toList();
    }
}
