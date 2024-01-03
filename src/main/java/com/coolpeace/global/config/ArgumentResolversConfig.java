package com.coolpeace.global.config;

import com.coolpeace.global.resolver.AuthJwtCredentialArgumentResolver;
import com.coolpeace.global.resolver.AuthJwtPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ArgumentResolversConfig implements WebMvcConfigurer {
    private final AuthJwtPrincipalArgumentResolver authJwtPrincipalArgumentResolver;
    private final AuthJwtCredentialArgumentResolver authJwtCredentialArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authJwtPrincipalArgumentResolver);
        resolvers.add(authJwtCredentialArgumentResolver);
    }
}
