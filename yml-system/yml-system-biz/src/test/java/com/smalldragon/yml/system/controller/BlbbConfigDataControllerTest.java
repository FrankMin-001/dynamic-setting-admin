package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.configdata.BlbbConfigDataDO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataCreateDTO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataPageDTO;
import com.smalldragon.yml.system.dal.configdata.VO.BlbbConfigDataVO;
import com.smalldragon.yml.system.service.configdata.BlbbConfigDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BlbbConfigDataController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbConfigDataControllerTest {

    @Mock
    private BlbbConfigDataService blbbConfigDataService;

    @InjectMocks
    private BlbbConfigDataController blbbConfigDataController;

    private BlbbConfigDataDO mockConfigData;
    private BlbbConfigDataVO mockConfigDataVO;

    @BeforeEach
    void setUp() {
        mockConfigData = new BlbbConfigDataDO();
        mockConfigData.setId(1L);
        mockConfigData.setTemplateType("test_type");

        mockConfigDataVO = new BlbbConfigDataVO();
        mockConfigDataVO.setId(1L);
        mockConfigDataVO.setTemplateType("test_type");
    }

    @Test
    void testInsertData() {
        BlbbConfigDataCreateDTO createDTO = new BlbbConfigDataCreateDTO();
        createDTO.setTemplateType("new_type");

        when(blbbConfigDataService.insertData(any(BlbbConfigDataCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbConfigDataController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        when(blbbConfigDataService.updateData(anyLong(), anyString(), anyString())).thenReturn(true);

        CommonResult<Boolean> result = blbbConfigDataController.updateData(1L, "row_data", "description");

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).updateData(1L, "row_data", "description");
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbConfigDataService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbConfigDataController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbConfigDataService.getInfoById(anyLong())).thenReturn(mockConfigDataVO);

        CommonResult<BlbbConfigDataVO> result = blbbConfigDataController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getTemplateType());
        verify(blbbConfigDataService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbConfigDataPageDTO pageDTO = new BlbbConfigDataPageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbConfigDataDO> mockPage = mock(IPage.class);
        when(blbbConfigDataService.pageList(any(BlbbConfigDataPageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbConfigDataDO>> result = blbbConfigDataController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).pageList(pageDTO);
    }

    @Test
    void testGetConfigDataByTemplateType() {
        List<BlbbConfigDataVO> mockList = Arrays.asList(mockConfigDataVO);
        when(blbbConfigDataService.getConfigDataByTemplateType(anyString())).thenReturn(mockList);

        CommonResult<List<BlbbConfigDataVO>> result = blbbConfigDataController.getConfigDataByTemplateType("test_type");

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).getConfigDataByTemplateType("test_type");
    }

    @Test
    void testToggleActive() {
        when(blbbConfigDataService.toggleActive(anyLong(), anyBoolean())).thenReturn(true);

        CommonResult<Boolean> result = blbbConfigDataController.toggleActive(1L, true);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbConfigDataService, times(1)).toggleActive(1L, true);
    }
}