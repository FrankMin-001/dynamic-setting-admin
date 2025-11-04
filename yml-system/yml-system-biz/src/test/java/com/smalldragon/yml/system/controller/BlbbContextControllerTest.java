package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.context.BlbbContextDO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextCreateDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextPageDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextUpdateDTO;
import com.smalldragon.yml.system.dal.context.VO.BlbbContextVO;
import com.smalldragon.yml.system.service.context.BlbbContextService;
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
public class BlbbContextControllerTest {

    @Mock
    private BlbbContextService blbbContextService;

    @InjectMocks
    private BlbbContextController controller;

    @Test
    void insertData_shouldReturn2000() {
        BlbbContextCreateDTO dto = new BlbbContextCreateDTO();
        when(blbbContextService.insertData(dto)).thenReturn(true);

        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturn2000() {
        BlbbContextUpdateDTO dto = new BlbbContextUpdateDTO();
        when(blbbContextService.updateData(dto)).thenReturn(true);

        CommonResult<Boolean> result = controller.updateData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbContextService.deleteData(Arrays.asList("1", "2"))).thenReturn(true);
        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1", "2"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void getInfoById_shouldReturnVO() {
        BlbbContextVO vo = new BlbbContextVO();
        when(blbbContextService.getInfoById("1")).thenReturn(vo);

        CommonResult<BlbbContextVO> result = controller.getInfoById("1");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(vo, result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbContextPageDTO pageDTO = new BlbbContextPageDTO();
        IPage<BlbbContextDO> page = mock(IPage.class);
        when(blbbContextService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbContextDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }

    @Test
    void getContextTree_shouldReturnList() {
        List<BlbbContextVO> list = Arrays.asList(new BlbbContextVO());
        when(blbbContextService.getContextTree()).thenReturn(list);
        CommonResult<List<BlbbContextVO>> result = controller.getContextTree();
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void getContextByPath_shouldReturnDO() {
        BlbbContextDO data = new BlbbContextDO();
        when(blbbContextService.getContextByPath("/a/b")).thenReturn(data);
        CommonResult<BlbbContextDO> result = controller.getContextByPath("/a/b");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(data, result.getData());
    }
}

