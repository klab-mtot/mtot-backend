package org.konkuk.klab.mtot.config;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.oauth.AuthenticationPrincipalArgumentResolver;
import org.konkuk.klab.mtot.oauth.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/members/**")
                .excludePathPatterns("/login/**", "/members/all");
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
