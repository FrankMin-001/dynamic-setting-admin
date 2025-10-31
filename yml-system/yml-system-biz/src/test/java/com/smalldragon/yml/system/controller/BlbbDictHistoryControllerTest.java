package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicthistory.BlbbDictHistoryDO;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BlbbDictHistoryController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbDictHistoryControllerTest {

    @Mock
    private BlbbDictHistoryService blbbDictHistoryService;

    @InjectMocks
    private BlbbDictHistoryController blbbDictHistoryController;

    @Test
    void testPageList() {
        IPage<BlbbDictHistoryDO> mockPage = mock(IPage.class);
        when(blbbDictHistoryService.pageList(anyInt(), anyInt(), any())).thenReturn(mockPage);

        CommonResult<IPage<BlbbDictHistoryDO>> result = blbbDictHistoryController.pageList(1, 10, null);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictHistoryService, times(1)).pageList(1, 10, null);
    }

    @Test
    void testPageListWithDictType() {
        IPage<BlbbDictHistoryDO> mockPage = mock(IPage.class);
        when(blbbDictHistoryService.pageList(anyInt(), anyInt(), anyString())).thenReturn(mockPage);

        CommonResult<IPage<BlbbDictHistoryDO>> result = blbbDictHistoryController.pageList(1, 10, "test_type");

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictHistoryService, times(1)).pageList(1, 10, "test_type");
    }
}