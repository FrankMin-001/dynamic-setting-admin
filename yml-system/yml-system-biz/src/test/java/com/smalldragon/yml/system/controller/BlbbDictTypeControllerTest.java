package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicttype.BlbbDictTypeDO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypeCreateDTO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypePageDTO;
import com.smalldragon.yml.system.service.dicttype.BlbbDictTypeService;
import com.smalldragon.yml.system.dal.dicttype.VO.BlbbDictTypeVO;
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
public class BlbbDictTypeControllerTest {

    @Mock
    private BlbbDictTypeService blbbDictTypeService;

    @InjectMocks
    private BlbbDictTypeController controller;

    @Test
    void insertData_shouldReturnTrue() {
        BlbbDictTypeCreateDTO dto = new BlbbDictTypeCreateDTO();
        when(blbbDictTypeService.insertData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturnTrue() {
        BlbbDictTypeCreateDTO dto = new BlbbDictTypeCreateDTO();
        when(blbbDictTypeService.updateData("1", dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.updateData("1", dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbDictTypeService.deleteData(Arrays.asList("1", "2"))).thenReturn(true);
        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1", "2"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void getDictTypeByType_shouldReturnDO() {
        BlbbDictTypeDO data = new BlbbDictTypeDO();
        when(blbbDictTypeService.getDictTypeByType("A")).thenReturn(data);
        CommonResult<BlbbDictTypeDO> result = controller.getDictTypeByType("A");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(data, result.getData());
    }

    @Test
    void getAllEnabledDictTypes_shouldReturnList() {
        List<BlbbDictTypeVO> list = Arrays.asList(new BlbbDictTypeVO());
        when(blbbDictTypeService.getAllEnabledDictTypes()).thenReturn(list);
        CommonResult<List<BlbbDictTypeVO>> result = controller.getAllEnabledDictTypes();
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void toggleStatus_shouldReturnTrue() {
        when(blbbDictTypeService.toggleStatus("1", 1)).thenReturn(true);
        CommonResult<Boolean> result = controller.toggleStatus("1", 1);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbDictTypePageDTO pageDTO = new BlbbDictTypePageDTO();
        IPage<BlbbDictTypeDO> page = mock(IPage.class);
        when(blbbDictTypeService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbDictTypeDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
