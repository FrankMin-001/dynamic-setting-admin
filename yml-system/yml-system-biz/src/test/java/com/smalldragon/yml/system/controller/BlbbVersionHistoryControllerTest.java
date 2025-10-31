package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import com.smalldragon.yml.system.controller.BlbbVersionHistoryController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BlbbVersionHistoryController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbVersionHistoryControllerTest {

    @Mock
    private BlbbVersionHistoryService blbbVersionHistoryService;

    @InjectMocks
    private BlbbVersionHistoryController blbbVersionHistoryController;

    @Test
    void testPageList() {
        IPage<BlbbVersionHistoryDO> mockPage = mock(IPage.class);
        when(blbbVersionHistoryService.pageList(anyInt(), anyInt(), any())).thenReturn(mockPage);

        CommonResult<IPage<BlbbVersionHistoryDO>> result = blbbVersionHistoryController.pageList(1, 10, null);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbVersionHistoryService, times(1)).pageList(1, 10, null);
    }

    @Test
    void testPageListWithConfigDataId() {
        IPage<BlbbVersionHistoryDO> mockPage = mock(IPage.class);
        when(blbbVersionHistoryService.pageList(anyInt(), anyInt(), anyLong())).thenReturn(mockPage);

        CommonResult<IPage<BlbbVersionHistoryDO>> result = blbbVersionHistoryController.pageList(1, 10, 123L);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbVersionHistoryService, times(1)).pageList(1, 10, 123L);
    }
}