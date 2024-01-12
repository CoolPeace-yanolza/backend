package com.coolpeace.api.global.config;

import com.coolpeace.api.domain.coupon.dto.request.converter.CouponRoomTypeRequestConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.CustomerTypeRequestConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.DayOfWeekRequestConverter;
import com.coolpeace.api.domain.coupon.dto.request.converter.DiscountTypeRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // coupon requests
        registry.addConverter(new CustomerTypeRequestConverter());
        registry.addConverter(new DiscountTypeRequestConverter());
        registry.addConverter(new CouponRoomTypeRequestConverter());
        registry.addConverter(new DayOfWeekRequestConverter());
    }
}
