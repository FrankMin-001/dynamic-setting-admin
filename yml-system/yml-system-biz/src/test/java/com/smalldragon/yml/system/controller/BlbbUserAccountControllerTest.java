package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountCreateDTO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountPageDTO;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlbbUserAccountControllerTest {

    @Mock
    private BlbbUserAccountService blbbUserAccountService;

    @InjectMocks
    private BlbbUserAccountController controller;

    @Test
    void insertData_shouldReturnTrue() {
        BlbbUserAccountCreateDTO dto = new BlbbUserAccountCreateDTO();
        when(blbbUserAccountService.insertData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updatePassword_shouldReturnTrue() {
        when(blbbUserAccountService.updatePassword("1", "newPwd")).thenReturn(true);
        CommonResult<Boolean> result = controller.updatePassword("1", "newPwd");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void getUserAccountByUsername_shouldReturnDO() {
        BlbbUserAccountDO data = new BlbbUserAccountDO();
        when(blbbUserAccountService.getUserAccountByUsername("u")).thenReturn(data);
        CommonResult<BlbbUserAccountDO> result = controller.getUserAccountByUsername("u");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(data, result.getData());
    }

    @Test
    void validateLogin_shouldReturnTrue() {
        when(blbbUserAccountService.validateLogin("u", "p")).thenReturn(true);
        CommonResult<Boolean> result = controller.validateLogin("u", "p");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbUserAccountPageDTO pageDTO = new BlbbUserAccountPageDTO();
        IPage<BlbbUserAccountDO> page = mock(IPage.class);
        when(blbbUserAccountService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbUserAccountDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
