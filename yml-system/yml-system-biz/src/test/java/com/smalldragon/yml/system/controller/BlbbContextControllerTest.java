package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.context.BlbbContextDO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextCreateDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextPageDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextUpdateDTO;
import com.smalldragon.yml.system.dal.context.VO.BlbbContextVO;
import com.smalldragon.yml.system.service.context.BlbbContextService;
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
 * BlbbContextController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbContextControllerTest {

    @Mock
    private BlbbContextService blbbContextService;

    @InjectMocks
    private BlbbContextController blbbContextController;

    private BlbbContextDO mockContext;
    private BlbbContextVO mockContextVO;

    @BeforeEach
    void setUp() {
        mockContext = new BlbbContextDO();
        mockContext.setId(1L);
        mockContext.setContextPath("test/path");

        mockContextVO = new BlbbContextVO();
        mockContextVO.setId(1L);
        mockContextVO.setContextPath("test/path");
    }

    @Test
    void testInsertData() {
        BlbbContextCreateDTO createDTO = new BlbbContextCreateDTO();
        createDTO.setContextPath("new/path");

        when(blbbContextService.insertData(any(BlbbContextCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbContextController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbContextService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        BlbbContextUpdateDTO updateDTO = new BlbbContextUpdateDTO();
        updateDTO.setId(1L);

        when(blbbContextService.updateData(any(BlbbContextUpdateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbContextController.updateData(updateDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbContextService, times(1)).updateData(updateDTO);
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbContextService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbContextController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbContextService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbContextService.getInfoById(anyLong())).thenReturn(mockContextVO);

        CommonResult<BlbbContextVO> result = blbbContextController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test/path", result.getData().getContextPath());
        verify(blbbContextService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbContextPageDTO pageDTO = new BlbbContextPageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbContextDO> mockPage = mock(IPage.class);
        when(blbbContextService.pageList(any(BlbbContextPageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbContextDO>> result = blbbContextController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbContextService, times(1)).pageList(pageDTO);
    }

    @Test
    void testGetContextTree() {
        List<BlbbContextVO> mockList = Arrays.asList(mockContextVO);
        when(blbbContextService.getContextTree()).thenReturn(mockList);

        CommonResult<List<BlbbContextVO>> result = blbbContextController.getContextTree();

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbContextService, times(1)).getContextTree();
    }

    @Test
    void testGetContextByPath() {
        when(blbbContextService.getContextByPath(anyString())).thenReturn(mockContext);

        CommonResult<BlbbContextDO> result = blbbContextController.getContextByPath("test/path");

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test/path", result.getData().getContextPath());
        verify(blbbContextService, times(1)).getContextByPath("test/path");
    }
}