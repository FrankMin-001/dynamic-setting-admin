package com.smalldragon.yml.filter;

import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.exception.NotLoginException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private final AuthenticateInterceptor interceptor = new AuthenticateInterceptor();

    @AfterEach
    void tearDown() {
        // 清理用户上下文，避免测试间互相影响
        UserContext.clearCache();
    }

    @Test
    void preHandle_ShouldPass_ForAuthPaths() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/blbb/auth/login");

        boolean result = interceptor.preHandle(request, response, new Object());

        Assertions.assertTrue(result, "认证路径应直接放行");
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void preHandle_ShouldBlockAndWrite401_WhenNotLoggedIn() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/blbb/dict/list");
        when(request.getSession(false)).thenReturn(null);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        boolean result = interceptor.preHandle(request, response, new Object());

        Assertions.assertFalse(result, "未登录应被拦截");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json;charset=UTF-8");
        String body = sw.toString();
        Assertions.assertTrue(body.contains("\"code\":401"), "响应体应包含401代码");
        Assertions.assertTrue(body.contains("未登录或登录已过期"), "响应体应提示未登录");
    }

    @Test
    void preHandle_ShouldPassAndSetUserContext_WhenLoggedIn() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/blbb/dict/list");
        when(request.getSession(false)).thenReturn(session);

        LoginDTO login = new LoginDTO();
        login.setId("u-1001");
        login.setUsername("tester");
        when(session.getAttribute("currentUser")).thenReturn(login);

        boolean result = interceptor.preHandle(request, response, new Object());

        Assertions.assertTrue(result, "登录状态应放行");
        Assertions.assertEquals("tester", UserContext.getLoginUsername(), "应设置用户上下文用户名");
    }

    @Test
    void afterCompletion_ShouldClearUserContext() {
        LoginDTO login = new LoginDTO();
        login.setId("u-1002");
        login.setUsername("clear-me");
        UserContext.setCurrentUser(login);

        interceptor.afterCompletion(null, null, null, null);

        Assertions.assertThrows(NotLoginException.class, UserContext::getCurrentUser, "应清理用户上下文");
    }
}

