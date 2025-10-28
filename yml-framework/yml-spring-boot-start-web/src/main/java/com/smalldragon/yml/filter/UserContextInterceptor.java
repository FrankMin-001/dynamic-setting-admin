package com.smalldragon.yml.filter;

import com.smalldragon.yml.context.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        // 请求开始时加载用户信息
        try {
            // 提前缓存
            UserContext.getLoginUser();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理缓存（无论成功或异常）
        UserContext.clearCache();
    }

}

