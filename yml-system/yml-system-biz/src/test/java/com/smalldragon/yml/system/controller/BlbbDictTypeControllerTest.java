package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicttype.BlbbDictTypeDO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypeCreateDTO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypePageDTO;
import com.smalldragon.yml.system.dal.dicttype.VO.BlbbDictTypeVO;
import com.smalldragon.yml.system.service.dicttype.BlbbDictTypeService;
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
 * BlbbDictTypeController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbDictTypeControllerTest {

    @Mock
    private BlbbDictTypeService blbbDictTypeService;

    @InjectMocks
    private BlbbDictTypeController blbbDictTypeController;

    private BlbbDictTypeDO mockDictType;
    private BlbbDictTypeVO mockDictTypeVO;

    @BeforeEach
    void setUp() {
        mockDictType = new BlbbDictTypeDO();
        mockDictType.setId(1L);
        mockDictType.setDictType("test_type");

        mockDictTypeVO = new BlbbDictTypeVO();
        mockDictTypeVO.setId(1L);
        mockDictTypeVO.setDictType("test_type");
    }

    @Test
    void testInsertData() {
        BlbbDictTypeCreateDTO createDTO = new BlbbDictTypeCreateDTO();
        createDTO.setDictType("new_type");

        when(blbbDictTypeService.insertData(any(BlbbDictTypeCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbDictTypeController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        BlbbDictTypeCreateDTO updateDTO = new BlbbDictTypeCreateDTO();
        updateDTO.setDictType("updated_type");

        when(blbbDictTypeService.updateData(anyLong(), any(BlbbDictTypeCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbDictTypeController.updateData(1L, updateDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).updateData(1L, updateDTO);
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbDictTypeService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbDictTypeController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbDictTypeService.getInfoById(anyLong())).thenReturn(mockDictTypeVO);

        CommonResult<BlbbDictTypeVO> result = blbbDictTypeController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getDictType());
        verify(blbbDictTypeService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbDictTypePageDTO pageDTO = new BlbbDictTypePageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbDictTypeDO> mockPage = mock(IPage.class);
        when(blbbDictTypeService.pageList(any(BlbbDictTypePageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbDictTypeDO>> result = blbbDictTypeController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).pageList(pageDTO);
    }

    @Test
    void testGetDictTypeByType() {
        when(blbbDictTypeService.getDictTypeByType(anyString())).thenReturn(mockDictType);

        CommonResult<BlbbDictTypeDO> result = blbbDictTypeController.getDictTypeByType("test_type");

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getDictType());
        verify(blbbDictTypeService, times(1)).getDictTypeByType("test_type");
    }

    @Test
    void testGetAllEnabledDictTypes() {
        List<BlbbDictTypeVO> mockList = Arrays.asList(mockDictTypeVO);
        when(blbbDictTypeService.getAllEnabledDictTypes()).thenReturn(mockList);

        CommonResult<List<BlbbDictTypeVO>> result = blbbDictTypeController.getAllEnabledDictTypes();

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).getAllEnabledDictTypes();
    }

    @Test
    void testToggleStatus() {
        when(blbbDictTypeService.toggleStatus(anyLong(), anyInt())).thenReturn(true);

        CommonResult<Boolean> result = blbbDictTypeController.toggleStatus(1L, 1);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbDictTypeService, times(1)).toggleStatus(1L, 1);
    }
}