package com.smalldragon.yml.filter;

import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 认证拦截器 - 最高优先级
 * 负责登录状态验证和用户信息设置
 *
 * @Author YML
 * @Date 2025/10/29
 */
@Slf4j
@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 排除登录相关接口和页面
        if (requestURI.startsWith("/api/blbb/auth/") ||
                requestURI.equals("/blbb/login") ||
                requestURI.startsWith("/swagger") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/doc.html") ||
                requestURI.startsWith("/system/userAccount/insertData")
        ) {
            return true;
        }

        // 检查登录状态
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            log.warn("未授权访问: {}", requestURI);

            // 如果是页面请求（非AJAX），重定向到登录页
            // 判断是否为AJAX请求：检查请求头
            String requestedWith = request.getHeader("X-Requested-With");
            boolean isAjaxRequest = "XMLHttpRequest".equals(requestedWith) ||
                    request.getHeader("Accept") != null &&
                            request.getHeader("Accept").contains("application/json");

            if (!isAjaxRequest && requestURI.startsWith("/blbb/")) {
                // 页面请求，重定向到登录页
                response.sendRedirect("/blbb/login?redirect=" + java.net.URLEncoder.encode(requestURI, "UTF-8"));
                return false;
            }

            // AJAX请求或API请求，抛出异常，由全局异常处理器处理
            throw new NotLoginException();
        }

        // 热刷新 Session 有效期
        // 通过重新获取 Session（如果已存在）来刷新最后访问时间，延长 Session 过期时间
        // request.getSession() 在 Session 已存在时会更新 lastAccessedTime，从而刷新有效期
        try {
            request.getSession();
        } catch (Exception e) {
            log.warn("刷新 Session 有效期失败: {}", e.getMessage());
        }

        // 设置用户上下文
        try {
            Object currentUser = session.getAttribute("currentUser");
            UserContext.setCurrentUser(currentUser);
            log.debug("用户认证通过: {}, URI: {}, Session已刷新", UserContext.getLoginUsername(), requestURI);
        } catch (Exception e) {
            log.error("设置用户上下文失败: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":500,\"message\":\"系统内部错误\"}");
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
