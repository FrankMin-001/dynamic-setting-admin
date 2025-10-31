package com.smalldragon.yml.filter;

import com.smalldragon.yml.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 认证拦截器 - 最高优先级
 * 负责登录状态验证和用户信息设置
 * @Author YML
 * @Date 2025/10/29
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 排除登录相关接口
        if (requestURI.startsWith("/api/blbb/auth/") ||
            requestURI.equals("/api/blbb/auth") ||
            requestURI.startsWith("/swagger") ||
            requestURI.startsWith("/webjars") ||
            requestURI.startsWith("/v2/api-docs") ||
            requestURI.startsWith("/doc.html")) {
            return true;
        }

        // 检查登录状态
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            response.setContentType("application/json;charset=UTF-8");
            log.warn("未授权访问: {}", requestURI);
            return false;
        }

        // 设置用户上下文
        try {
            Object currentUser = session.getAttribute("currentUser");
            UserContext.setCurrentUser(currentUser);
            log.debug("用户认证通过: {}, URI: {}", UserContext.getLoginUsername(), requestURI);
        } catch (Exception e) {
            log.error("设置用户上下文失败: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"code\":500,\"message\":\"系统内部错误\"}");
            response.setContentType("application/json;charset=UTF-8");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理用户上下文
        UserContext.clearCache();
        log.debug("请求处理完成，清理用户上下文");
    }
}
