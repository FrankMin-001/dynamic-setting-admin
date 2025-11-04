package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.title.BlbbTitleDO;
import com.smalldragon.yml.system.dal.title.VO.BlbbTitleVO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleCreateDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitlePageDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleUpdateDTO;
import com.smalldragon.yml.system.service.title.BlbbTitleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlbbTitleControllerTest {

    @Mock
    private BlbbTitleService blbbTitleService;

    @InjectMocks
    private BlbbTitleController controller;

    @Test
    void insertData_shouldReturnTrue() {
        BlbbTitleCreateDTO dto = new BlbbTitleCreateDTO();
        when(blbbTitleService.insertData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturnTrue() {
        BlbbTitleUpdateDTO dto = new BlbbTitleUpdateDTO();
        when(blbbTitleService.updateData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.updateData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbTitleService.deleteData(Arrays.asList("1"))).thenReturn(true);
        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void listByContextId_shouldReturnList() {
        List<BlbbTitleVO> list = Arrays.asList(new BlbbTitleVO());
        when(blbbTitleService.listByContextId("2")).thenReturn(list);
        CommonResult<List<BlbbTitleVO>> result = controller.listByContextId("2");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void getByTitleKey_shouldReturnDO() {
        BlbbTitleDO data = new BlbbTitleDO();
        when(blbbTitleService.getByTitleKey("k")).thenReturn(data);
        CommonResult<BlbbTitleDO> result = controller.getByTitleKey("k");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(data, result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbTitlePageDTO pageDTO = new BlbbTitlePageDTO();
        IPage<BlbbTitleDO> page = mock(IPage.class);
        when(blbbTitleService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbTitleDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
