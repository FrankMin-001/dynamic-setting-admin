package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dictdata.BlbbDictDataDO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataUpdateDTO;
import com.smalldragon.yml.system.dal.dictdata.VO.BlbbDictDataVO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataCreateDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataPageDTO;
import com.smalldragon.yml.system.service.dictdata.BlbbDictDataService;
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
public class BlbbDictDataControllerTest {

    @Mock
    private BlbbDictDataService blbbDictDataService;

    @InjectMocks
    private BlbbDictDataController controller;

    @Test
    void insertData_shouldReturnTrue() {
        BlbbDictDataCreateDTO dto = new BlbbDictDataCreateDTO();
        when(blbbDictDataService.insertData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturnTrue() {
        BlbbDictDataUpdateDTO dto = new BlbbDictDataUpdateDTO();
        when(blbbDictDataService.updateData("1", dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.updateData("1", dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbDictDataService.deleteData(Arrays.asList("1", "2"))).thenReturn(true);
        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1", "2"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void listByDictType_shouldReturnList() {
        List<BlbbDictDataVO> list = Arrays.asList(new BlbbDictDataVO());
        when(blbbDictDataService.listByDictType("t")).thenReturn(list);
        CommonResult<List<BlbbDictDataVO>> result = controller.listByDictType("t");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void toggleStatus_shouldReturnTrue() {
        when(blbbDictDataService.toggleStatus("1", 1)).thenReturn(true);
        CommonResult<Boolean> result = controller.toggleStatus("1", 1);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbDictDataPageDTO pageDTO = new BlbbDictDataPageDTO();
        IPage<BlbbDictDataDO> page = mock(IPage.class);
        when(blbbDictDataService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbDictDataDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
