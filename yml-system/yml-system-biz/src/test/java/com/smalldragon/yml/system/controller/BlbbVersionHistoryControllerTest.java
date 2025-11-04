package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlbbVersionHistoryControllerTest {

    @Mock
    private BlbbVersionHistoryService blbbVersionHistoryService;

    @InjectMocks
    private BlbbVersionHistoryController controller;

    @Test
    void pageList_shouldReturnPage() {
        IPage<BlbbVersionHistoryDO> page = mock(IPage.class);
        when(blbbVersionHistoryService.pageList(1, 10, String.valueOf(100L))).thenReturn(page);
        CommonResult<IPage<BlbbVersionHistoryDO>> result = controller.pageList(1, 10, "100","100");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}

