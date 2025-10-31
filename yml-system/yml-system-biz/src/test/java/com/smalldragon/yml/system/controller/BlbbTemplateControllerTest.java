package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.template.BlbbTemplateDO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateCreateDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplatePageDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateUpdateDTO;
import com.smalldragon.yml.system.dal.template.VO.BlbbTemplateVO;
import com.smalldragon.yml.system.service.template.BlbbTemplateService;
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
 * BlbbTemplateController 测试类
 * @Author YML
 * @Date 2025/10/31
 */
@ExtendWith(MockitoExtension.class)
class BlbbTemplateControllerTest {

    @Mock
    private BlbbTemplateService blbbTemplateService;

    @InjectMocks
    private BlbbTemplateController blbbTemplateController;

    private BlbbTemplateDO mockTemplate;
    private BlbbTemplateVO mockTemplateVO;

    @BeforeEach
    void setUp() {
        mockTemplate = new BlbbTemplateDO();
        mockTemplate.setId(1L);
        mockTemplate.setTemplateType("test_type");

        mockTemplateVO = new BlbbTemplateVO();
        mockTemplateVO.setId(1L);
        mockTemplateVO.setTemplateType("test_type");
    }

    @Test
    void testInsertData() {
        BlbbTemplateCreateDTO createDTO = new BlbbTemplateCreateDTO();
        createDTO.setTemplateType("new_type");

        when(blbbTemplateService.insertData(any(BlbbTemplateCreateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbTemplateController.insertData(createDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTemplateService, times(1)).insertData(createDTO);
    }

    @Test
    void testUpdateData() {
        BlbbTemplateUpdateDTO updateDTO = new BlbbTemplateUpdateDTO();
        updateDTO.setId(1L);

        when(blbbTemplateService.updateData(any(BlbbTemplateUpdateDTO.class))).thenReturn(true);

        CommonResult<Boolean> result = blbbTemplateController.updateData(updateDTO);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTemplateService, times(1)).updateData(updateDTO);
    }

    @Test
    void testDeleteData() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(blbbTemplateService.deleteData(anyList())).thenReturn(true);

        CommonResult<Boolean> result = blbbTemplateController.deleteData(ids);

        assertTrue(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTemplateService, times(1)).deleteData(ids);
    }

    @Test
    void testGetInfoById() {
        when(blbbTemplateService.getInfoById(anyLong())).thenReturn(mockTemplateVO);

        CommonResult<BlbbTemplateVO> result = blbbTemplateController.getInfoById(1L);

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getTemplateType());
        verify(blbbTemplateService, times(1)).getInfoById(1L);
    }

    @Test
    void testPageList() {
        BlbbTemplatePageDTO pageDTO = new BlbbTemplatePageDTO();
        pageDTO.setPageNo(1);
        pageDTO.setPageSize(10);

        IPage<BlbbTemplateDO> mockPage = mock(IPage.class);
        when(blbbTemplateService.pageList(any(BlbbTemplatePageDTO.class))).thenReturn(mockPage);

        CommonResult<IPage<BlbbTemplateDO>> result = blbbTemplateController.pageList(pageDTO);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        verify(blbbTemplateService, times(1)).pageList(pageDTO);
    }

    @Test
    void testGetTemplateByType() {
        when(blbbTemplateService.getTemplateByType(anyString())).thenReturn(mockTemplate);

        CommonResult<BlbbTemplateDO> result = blbbTemplateController.getTemplateByType("test_type");

        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test_type", result.getData().getTemplateType());
        verify(blbbTemplateService, times(1)).getTemplateByType("test_type");
    }

    @Test
    void testGetAllTemplates() {
        List<BlbbTemplateVO> mockList = Arrays.asList(mockTemplateVO);
        when(blbbTemplateService.getAllTemplates()).thenReturn(mockList);

        CommonResult<List<BlbbTemplateVO>> result = blbbTemplateController.getAllTemplates();

        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(200, result.getCode());
        verify(blbbTemplateService, times(1)).getAllTemplates();
    }
}