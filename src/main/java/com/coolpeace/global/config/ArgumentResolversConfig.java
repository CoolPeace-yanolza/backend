package com.coolpeace.global.config;

import com.coolpeace.global.resolver.AuthMemberCredentialArgumentResolver;
import com.coolpeace.global.resolver.AuthMemberPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ArgumentResolversConfig implements WebMvcConfigurer {
    private final AuthMemberPrincipalArgumentResolver authMemberPrincipalArgumentResolver;
    private final AuthMemberCredentialArgumentResolver authMemberCredentialArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberPrincipalArgumentResolver);
        resolvers.add(authMemberCredentialArgumentResolver);
    }
}
