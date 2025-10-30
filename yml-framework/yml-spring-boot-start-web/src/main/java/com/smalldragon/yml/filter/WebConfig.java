package com.smalldragon.yml.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * @Author YML
 * @Date 2025/10/29
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticateInterceptor authenticateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 认证拦截器 - 最高优先级，负责登录验证
        registry.addInterceptor(authenticateInterceptor)
                .addPathPatterns("**")
                .excludePathPatterns("/api/blbb/auth/**", "/swagger/**", "/webjars/**", "/v2/api-docs", "/doc.html");
    }
}
