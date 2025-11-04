package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.configdata.BlbbConfigDataDO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataCreateDTO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataPageDTO;
import com.smalldragon.yml.system.dal.configdata.VO.BlbbConfigDataVO;
import com.smalldragon.yml.system.service.configdata.BlbbConfigDataService;
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
public class BlbbConfigDataControllerTest {

    @Mock
    private BlbbConfigDataService blbbConfigDataService;

    @InjectMocks
    private BlbbConfigDataController controller;

    @Test
    void insertData_shouldReturn2000() {
        BlbbConfigDataCreateDTO dto = new BlbbConfigDataCreateDTO();
        when(blbbConfigDataService.insertData(dto)).thenReturn(true);

        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturn2000() {
        when(blbbConfigDataService.updateData("1", "newRowData", "test description")).thenReturn(true);

        CommonResult<Boolean> result = controller.updateData("1", "newRowData", "test description");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbConfigDataPageDTO pageDTO = new BlbbConfigDataPageDTO();
        IPage<BlbbConfigDataDO> page = mock(IPage.class);
        when(blbbConfigDataService.pageList(pageDTO)).thenReturn(page);

        CommonResult<IPage<BlbbConfigDataDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }

    @Test
    void getInfoById_shouldReturnVO() {
        BlbbConfigDataVO vo = new BlbbConfigDataVO();
        when(blbbConfigDataService.getInfoById("1")).thenReturn(vo);

        CommonResult<BlbbConfigDataVO> result = controller.getInfoById("1");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(vo, result.getData());
    }

    @Test
    void getConfigDataByTemplateType_shouldReturnList() {
        List<BlbbConfigDataVO> list = Arrays.asList(new BlbbConfigDataVO());
        when(blbbConfigDataService.getConfigDataByTemplateType("t")).thenReturn(list);

        CommonResult<List<BlbbConfigDataVO>> result = controller.getConfigDataByTemplateType("t");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void getConfigDataByTitleId_shouldReturnList() {
        List<BlbbConfigDataVO> list = Arrays.asList(new BlbbConfigDataVO());
        when(blbbConfigDataService.getConfigDataByTitleId("1")).thenReturn(list);

        CommonResult<List<BlbbConfigDataVO>> result = controller.getConfigDataByTitleId("1");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void toggleActive_shouldReturnTrue() {
        when(blbbConfigDataService.toggleActive("1", true)).thenReturn(true);

        CommonResult<Boolean> result = controller.toggleActive("1", true);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbConfigDataService.deleteData(Arrays.asList("1", "2"))).thenReturn(true);

        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1", "2"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }
}

