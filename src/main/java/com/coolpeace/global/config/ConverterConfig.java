package com.coolpeace.global.config;

import com.coolpeace.domain.coupon.dto.request.converter.CouponRoomTypeRequestConverter;
import com.coolpeace.domain.coupon.dto.request.converter.CustomerTypeRequestConverter;
import com.coolpeace.domain.coupon.dto.request.converter.DayOfWeekRequestConverter;
import com.coolpeace.domain.coupon.dto.request.converter.DiscountTypeRequestConverter;
import com.coolpeace.domain.coupon.dto.response.converter.*;
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

        // coupon responses
        registry.addConverter(new CouponStatusTypeResponseConverter());
        registry.addConverter(new CustomerTypeResponseConverter());
        registry.addConverter(new DiscountTypeResponseConverter());
        registry.addConverter(new CouponRoomTypeResponseConverter());
        registry.addConverter(new DayOfWeekResponseConverter());
    }
}
