package com.coolpeace.api.global.config;

import com.coolpeace.api.domain.coupon.dto.request.converter.CouponRoomTypeConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.CustomerTypeConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.DayOfWeekConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.DiscountTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // coupons
        registry.addConverter(new CustomerTypeConverter());
        registry.addConverter(new DiscountTypeConverter());
        registry.addConverter(new CouponRoomTypeConverter());
        registry.addConverter(new DayOfWeekConverter());
    }
}
