package com.smalldragon.yml.system.controller;

import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        // Mock setup can be done here if needed
    }

    @Test
    void login_Success() {
        // Mock user data
        BlbbUserAccountDO mockUser = new BlbbUserAccountDO();
        mockUser.setUsername("testuser");
        mockUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy..."); // Encrypted password
        
        // Mock service layer behavior
        when(blbbUserAccountService.getUserAccountByUsername("testuser")).thenReturn(mockUser);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(true);
        
        // Execute test
        ResponseEntity<?> response = blbbLoginController.login("testuser", "password", mockRequest);
        
        // Verify result
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void login_Failure_WrongPassword() {
        // Mock user data
        BlbbUserAccountDO mockUser = new BlbbUserAccountDO();
        mockUser.setUsername("testuser");
        mockUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy...");
        
        // Mock service layer behavior
        when(blbbUserAccountService.getUserAccountByUsername("testuser")).thenReturn(mockUser);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(false);
        
        // Execute test
        ResponseEntity<?> response = blbbLoginController.login("testuser", "wrongpassword", mockRequest);
        
        // Verify result
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username or password incorrect", response.getBody());
    }

    @Test
    void logout_Success() {
        // Mock session
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("username")).thenReturn("testuser");
        
        // Execute test
        ResponseEntity<?> response = blbbLoginController.logout(mockRequest);
        
        // Verify result
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logout successful", response.getBody());
        verify(mockSession).invalidate();
    }

    @Test
    void checkLoginStatus_LoggedIn() {
        // Mock logged-in session
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("currentUser")).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testuser");
        
        // Execute test
        ResponseEntity<?> response = blbbLoginController.checkLoginStatus(mockRequest);
        
        // Verify result
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Logged in"));
    }
}