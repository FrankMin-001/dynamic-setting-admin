package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dictdata.BlbbDictDataDO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataCreateDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataPageDTO;
import com.smalldragon.yml.system.dal.dictdata.VO.BlbbDictDataVO;
import com.smalldragon.yml.system.service.dictdata.BlbbDictDataService;
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
 * BlbbDictDataController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbDictDataControllerTest {

    @Mock
    private BlbbDictDataService blbbDictDataService;

    @InjectMocks
    private BlbbDictDataController blbbDictDataController;

    private BlbbDictDataDO mockDictData;
    private BlbbDictDataVO mockDictDataVO;

    @BeforeEach
    void setUp() {
        mockDictData = new BlbbDictDataDO();
        mockDictData.setId(1L);
        mockDictData.setDictType("test_type");

        mockDictDataVO = new BlbbDictDataVO();
        mockDictDataVO.setId(1L);
        mockDictDataVO.setDictType("test_type");
    }

    @Test
    void testInsertData() {
        BlbbDictDataCreateDTO createDTO = new BlbbDictDataCreateDTO();
        createDTO.setDictType("new_type");

        when(blbbDictDataService.insertData(any(BlbbDictDataCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbDictDataController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        BlbbDictDataCreateDTO updateDTO = new BlbbDictDataCreateDTO();
        updateDTO.setDictType("updated_type");

        when(blbbDictDataService.updateData(anyLong(), any(BlbbDictDataCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbDictDataController.updateData(1L, updateDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).updateData(1L, updateDTO);
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbDictDataService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbDictDataController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbDictDataService.getInfoById(anyLong())).thenReturn(mockDictDataVO);

        CommonResult<BlbbDictDataVO> result = blbbDictDataController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getDictType());
        verify(blbbDictDataService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbDictDataPageDTO pageDTO = new BlbbDictDataPageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbDictDataDO> mockPage = mock(IPage.class);
        when(blbbDictDataService.pageList(any(BlbbDictDataPageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbDictDataDO>> result = blbbDictDataController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).pageList(pageDTO);
    }

    @Test
    void testListByDictType() {
        List<BlbbDictDataVO> mockList = Arrays.asList(mockDictDataVO);
        when(blbbDictDataService.listByDictType(anyString())).thenReturn(mockList);

        CommonResult<List<BlbbDictDataVO>> result = blbbDictDataController.listByDictType("test_type");

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).listByDictType("test_type");
    }

    @Test
    void testToggleStatus() {
        when(blbbDictDataService.toggleStatus(anyLong(), anyInt())).thenReturn(true);

        CommonResult<Boolean> result = blbbDictDataController.toggleStatus(1L, 1);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictDataService, times(1)).toggleStatus(1L, 1);
    }
}