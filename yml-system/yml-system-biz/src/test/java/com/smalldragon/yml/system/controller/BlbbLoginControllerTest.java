package com.smalldragon.yml.system.controller;

import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import com.smalldragon.yml.system.util.PasswordUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlbbLoginControllerTest {

    @Mock
    private BlbbUserAccountService blbbUserAccountService;

    @InjectMocks
    private BlbbLoginController controller;

    @Test
    void login_success_shouldReturnOk() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(session);

        BlbbUserAccountDO user = new BlbbUserAccountDO();
        user.setId("1");
        user.setUsername("tom");
        user.setPassword(PasswordUtil.encode("pwd"));
        when(blbbUserAccountService.getUserAccountByUsername("tom")).thenReturn(user);

        ResponseEntity<?> resp = controller.login("tom", "pwd", request);
        Assertions.assertEquals(200, resp.getStatusCodeValue());
        verify(session).setAttribute(eq("currentUser"), any());
        verify(session).setAttribute("userId", "1");
        verify(session).setAttribute("username", "tom");
    }

    @Test
    void login_failed_shouldReturnBadRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        BlbbUserAccountDO user = new BlbbUserAccountDO();
        user.setUsername("jerry");
        user.setPassword(PasswordUtil.encode("pwd"));
        when(blbbUserAccountService.getUserAccountByUsername("jerry")).thenReturn(user);

        ResponseEntity<?> resp = controller.login("jerry", "wrong", request);
        Assertions.assertEquals(400, resp.getStatusCodeValue());
    }

    @Test
    void logout_shouldInvalidateSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);

        ResponseEntity<?> resp = controller.logout(request);
        Assertions.assertEquals(200, resp.getStatusCodeValue());
        verify(session).invalidate();
    }

    @Test
    void status_loggedIn_shouldReturnMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        LoginDTO login = new LoginDTO();
        // 使用反射或者直接 mock LoginDTO 的 getUsername 方法
        LoginDTO mockLogin = mock(LoginDTO.class);
        when(mockLogin.getUsername()).thenReturn("alice");
        when(session.getAttribute("currentUser")).thenReturn(mockLogin);

        ResponseEntity<?> resp = controller.checkLoginStatus(request);
        Assertions.assertEquals(200, resp.getStatusCodeValue());
        Assertions.assertTrue(((String) resp.getBody()).contains("已登录"));
    }

    @Test
    void status_notLoggedIn_shouldReturnMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(null);

        ResponseEntity<?> resp = controller.checkLoginStatus(request);
        Assertions.assertEquals(200, resp.getStatusCodeValue());
        Assertions.assertTrue(((String) resp.getBody()).contains("未登录"));
    }
}
