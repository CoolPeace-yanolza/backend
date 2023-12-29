package com.coolpeace.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private static final String ALL_PERMIT = "*";
    private static final List<String> ALL_PERMIT_LIST = Collections.singletonList(ALL_PERMIT);
    private static final List<String> ALLOW_METHODS = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Spring Web Cors 설정
        registry.addMapping("/**")
                .allowedOriginPatterns(ALL_PERMIT)
                .allowedMethods(ALLOW_METHODS.toArray(String[]::new));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Spring Security Cors 설정
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedMethods(ALLOW_METHODS);
        config.setAllowedOriginPatterns(ALL_PERMIT_LIST);
        config.setAllowedHeaders(ALL_PERMIT_LIST);
        config.setExposedHeaders(ALL_PERMIT_LIST);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
