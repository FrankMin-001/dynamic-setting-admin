package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountCreateDTO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountPageDTO;
import com.smalldragon.yml.system.dal.useraccount.VO.BlbbUserAccountVO;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BlbbUserAccountController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbUserAccountControllerTest {

    @Mock
    private BlbbUserAccountService blbbUserAccountService;

    @InjectMocks
    private BlbbUserAccountController blbbUserAccountController;

    private BlbbUserAccountDO mockUserAccount;
    private BlbbUserAccountVO mockUserAccountVO;

    @BeforeEach
    void setUp() {
        mockUserAccount = new BlbbUserAccountDO();
        mockUserAccount.setId(1L);
        mockUserAccount.setUsername("testuser");

        mockUserAccountVO = new BlbbUserAccountVO();
        mockUserAccountVO.setId(1L);
        mockUserAccountVO.setUsername("testuser");
    }

    @Test
    void testInsertData() {
        BlbbUserAccountCreateDTO createDTO = new BlbbUserAccountCreateDTO();
        createDTO.setUsername("newuser");

        when(blbbUserAccountService.insertData(any(BlbbUserAccountCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbUserAccountController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbUserAccountService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdatePassword() {
        when(blbbUserAccountService.updatePassword(anyLong(), anyString())).thenReturn(true);

        CommonResult<Boolean> result = blbbUserAccountController.updatePassword(1L, "newpassword");

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbUserAccountService, times(1)).updatePassword(1L, "newpassword");
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbUserAccountService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbUserAccountController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbUserAccountService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbUserAccountService.getInfoById(anyLong())).thenReturn(mockUserAccountVO);

        CommonResult<BlbbUserAccountVO> result = blbbUserAccountController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("testuser", result.getData().getUsername());
        verify(blbbUserAccountService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbUserAccountPageDTO pageDTO = new BlbbUserAccountPageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbUserAccountDO> mockPage = mock(IPage.class);
        when(blbbUserAccountService.pageList(any(BlbbUserAccountPageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbUserAccountDO>> result = blbbUserAccountController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbUserAccountService, times(1)).pageList(pageDTO);
    }

    @Test
    void testGetUserAccountByUsername() {
        when(blbbUserAccountService.getUserAccountByUsername(anyString())).thenReturn(mockUserAccount);

        CommonResult<BlbbUserAccountDO> result = blbbUserAccountController.getUserAccountByUsername("testuser");

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("testuser", result.getData().getUsername());
        verify(blbbUserAccountService, times(1)).getUserAccountByUsername("testuser");
    }

    @Test
    void testValidateLogin() {
        when(blbbUserAccountService.validateLogin(anyString(), anyString())).thenReturn(true);

        CommonResult<Boolean> result = blbbUserAccountController.validateLogin("testuser", "password");

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbUserAccountService, times(1)).validateLogin("testuser", "password");
    }
}
