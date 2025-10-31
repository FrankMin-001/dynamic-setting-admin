package com.smalldragon.yml.system.filter;

import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.filter.AuthenticateInterceptor;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthenticateInterceptor 测试类
 * @Author YML
 * @Date 2025/10/29
 */
@Slf4j
class AuthenticateInterceptorTest {

    private AuthenticateInterceptor authenticateInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        authenticateInterceptor = new AuthenticateInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        UserContext.clearCache(); // 清理缓存
    }

    @Test
    void testPreHandle_LoginPath_ShouldPass() throws Exception {
        // 设置登录路径
        request.setRequestURI("/api/blbb/auth/login");

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testPreHandle_SwaggerPath_ShouldPass() throws Exception {
        // 设置 Swagger 路径
        request.setRequestURI("/swagger-ui.html");

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testPreHandle_NoSession_ShouldReturnUnauthorized() throws Exception {
        // 设置需要认证的路径
        request.setRequestURI("/api/blbb/user/info");

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertFalse(result);
        assertEquals(401, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertTrue(response.getContentAsString().contains("未登录或登录已过期"));
    }

    @Test
    void testPreHandle_SessionWithoutUser_ShouldReturnUnauthorized() throws Exception {
        // 设置需要认证的路径
        request.setRequestURI("/api/blbb/user/info");

        // 创建空 session
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertFalse(result);
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("未登录或登录已过期"));
    }

    @Test
    void testPreHandle_ValidSession_ShouldPass() throws Exception {
        // 设置需要认证的路径
        request.setRequestURI("/api/blbb/user/info");

        // 创建有用户信息的 session
        MockHttpSession session = new MockHttpSession();
        BlbbUserAccountDO user = new BlbbUserAccountDO();
        user.setId(1L);
        user.setUsername("testuser");
        session.setAttribute("currentUser", user);
        session.setAttribute("username", "testuser");
        request.setSession(session);

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(200, response.getStatus());

        // 验证用户上下文已设置
        Object currentUser = UserContext.getCurrentUser();
        assertNotNull(currentUser);
        assertTrue(currentUser instanceof BlbbUserAccountDO);
        assertEquals("testuser", ((BlbbUserAccountDO) currentUser).getUsername());
    }

    @Test
    void testPreHandle_ApiDocsPath_ShouldPass() throws Exception {
        // 设置 API 文档路径
        request.setRequestURI("/v2/api-docs");

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testPreHandle_WebjarsPath_ShouldPass() throws Exception {
        // 设置 Webjars 路径
        request.setRequestURI("/webjars/js/chunk-vendors.js");

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testAfterCompletion_ShouldClearUserContext() {
        // 先设置用户上下文
        BlbbUserAccountDO user = new BlbbUserAccountDO();
        user.setId(1L);
        user.setUsername("testuser");
        UserContext.setCurrentUser(user);

        // 验证用户上下文已设置
        assertNotNull(UserContext.getCurrentUser());

        // 执行 afterCompletion
        authenticateInterceptor.afterCompletion(request, response, null, null);

        // 验证用户上下文已清理
        assertNull(UserContext.getCurrentUser());
    }

    @Test
    void testPreHandle_ExceptionInUserContext_ShouldReturnInternalError() throws Exception {
        // 设置需要认证的路径
        request.setRequestURI("/api/blbb/user/info");

        // 创建有用户信息的 session
        MockHttpSession session = new MockHttpSession();
        // 设置一个无法序列化的对象来模拟异常
        session.setAttribute("currentUser", new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("模拟异常");
            }
        });
        request.setSession(session);

        boolean result = authenticateInterceptor.preHandle(request, response, null);

        assertFalse(result);
        assertEquals(500, response.getStatus());
        assertTrue(response.getContentAsString().contains("系统内部错误"));
    }
}
