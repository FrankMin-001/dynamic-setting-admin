package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicthistory.BlbbDictHistoryDO;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlbbDictHistoryControllerTest {

    @Mock
    private BlbbDictHistoryService blbbDictHistoryService;

    @InjectMocks
    private BlbbDictHistoryController controller;

    @Test
    void pageList_shouldReturnPage() {
        IPage<BlbbDictHistoryDO> page = mock(IPage.class);
        when(blbbDictHistoryService.pageList(1, 10, "typeA")).thenReturn(page);
        CommonResult<IPage<BlbbDictHistoryDO>> result = controller.pageList(1, 10, "typeA");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
