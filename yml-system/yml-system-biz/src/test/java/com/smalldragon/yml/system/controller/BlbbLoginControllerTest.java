package com.smalldragon.yml.system.controller;

import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class BlbbLoginControllerTest {

    @Mock
    private BlbbUserAccountService blbbUserAccountService;
    
    @Mock
    private PasswordUtil passwordUtil;
    
    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpSession mockSession;
    
    @Mock
    private BlbbUserAccountDO mockUser;

    @InjectMocks
    private BlbbLoginController blbbLoginController;

    @Test
    void login_Success() {
        // 模拟用户数据
        BlbbUserAccountDO mockUser = new BlbbUserAccountDO();
        mockUser.setUsername("testuser");
        mockUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy..."); // 加密后的密码
        
        // 模拟服务层行为
        when(blbbUserAccountService.getUserAccountByUsername("testuser")).thenReturn(mockUser);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(true);
        
        // 执行测试
        ResponseEntity<?> response = blbbLoginController.login("testuser", "password", mockRequest);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("登录成功", response.getBody());
    }

    @Test
    void login_Failure_WrongPassword() {
        // 模拟用户数据
        BlbbUserAccountDO mockUser = new BlbbUserAccountDO();
        mockUser.setUsername("testuser");
        mockUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy...");
        
        // 模拟服务层行为
        when(blbbUserAccountService.getUserAccountByUsername("testuser")).thenReturn(mockUser);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(false);
        
        // 执行测试
        ResponseEntity<?> response = blbbLoginController.login("testuser", "wrongpassword", mockRequest);
        
        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("用户名或密码错误", response.getBody());
    }

    @Test
    void logout_Success() {
        // 模拟session
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("username")).thenReturn("testuser");
        
        // 执行测试
        ResponseEntity<?> response = blbbLoginController.logout(mockRequest);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("登出成功", response.getBody());
        verify(mockSession).invalidate();
    }

    @Test
    void checkLoginStatus_LoggedIn() {
        // 模拟已登录session
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("currentUser")).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testuser");
        
        // 执行测试
        ResponseEntity<?> response = blbbLoginController.checkLoginStatus(mockRequest);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("已登录"));
    }
}