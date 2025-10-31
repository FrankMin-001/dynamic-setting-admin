package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.title.BlbbTitleDO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleCreateDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitlePageDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleUpdateDTO;
import com.smalldragon.yml.system.dal.title.VO.BlbbTitleVO;
import com.smalldragon.yml.system.service.title.BlbbTitleService;
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
 * BlbbTitleController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbTitleControllerTest {

    @Mock
    private BlbbTitleService blbbTitleService;

    @InjectMocks
    private BlbbTitleController blbbTitleController;

    private BlbbTitleDO mockTitle;
    private BlbbTitleVO mockTitleVO;

    @BeforeEach
    void setUp() {
        mockTitle = new BlbbTitleDO();
        mockTitle.setId(1L);
        mockTitle.setTitleKey("test_key");

        mockTitleVO = new BlbbTitleVO();
        mockTitleVO.setId(1L);
        mockTitleVO.setTitleKey("test_key");
    }

    @Test
    void testInsertData() {
        BlbbTitleCreateDTO createDTO = new BlbbTitleCreateDTO();
        createDTO.setTitleKey("new_key");

        when(blbbTitleService.insertData(any(BlbbTitleCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbTitleController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTitleService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        BlbbTitleUpdateDTO updateDTO = new BlbbTitleUpdateDTO();
        updateDTO.setId(1L);

        when(blbbTitleService.updateData(any(BlbbTitleUpdateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbTitleController.updateData(updateDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTitleService, times(1)).updateData(updateDTO);
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbTitleService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbTitleController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTitleService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbTitleService.getInfoById(anyLong())).thenReturn(mockTitleVO);

        CommonResult<BlbbTitleVO> result = blbbTitleController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_key", result.getData().getTitleKey());
        verify(blbbTitleService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbTitlePageDTO pageDTO = new BlbbTitlePageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbTitleDO> mockPage = mock(IPage.class);
        when(blbbTitleService.pageList(any(BlbbTitlePageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbTitleDO>> result = blbbTitleController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTitleService, times(1)).pageList(pageDTO);
    }

    @Test
    void testListByContextId() {
        List<BlbbTitleVO> mockList = Arrays.asList(mockTitleVO);
        when(blbbTitleService.listByContextId(anyLong())).thenReturn(mockList);

        CommonResult<List<BlbbTitleVO>> result = blbbTitleController.listByContextId(1L);

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbTitleService, times(1)).listByContextId(1L);
    }

    @Test
    void testGetByTitleKey() {
        when(blbbTitleService.getByTitleKey(anyString())).thenReturn(mockTitle);

        CommonResult<BlbbTitleDO> result = blbbTitleController.getByTitleKey("test_key");

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_key", result.getData().getTitleKey());
        verify(blbbTitleService, times(1)).getByTitleKey("test_key");
    }
}