package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.template.BlbbTemplateDO;
import com.smalldragon.yml.system.dal.template.VO.BlbbTemplateVO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateCreateDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplatePageDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateUpdateDTO;
import com.smalldragon.yml.system.service.template.BlbbTemplateService;
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
public class BlbbTemplateControllerTest {

    @Mock
    private BlbbTemplateService blbbTemplateService;

    @InjectMocks
    private BlbbTemplateController controller;

    @Test
    void insertData_shouldReturnTrue() {
        BlbbTemplateCreateDTO dto = new BlbbTemplateCreateDTO();
        when(blbbTemplateService.insertData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.insertData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void updateData_shouldReturnTrue() {
        BlbbTemplateUpdateDTO dto = new BlbbTemplateUpdateDTO();
        when(blbbTemplateService.updateData(dto)).thenReturn(true);
        CommonResult<Boolean> result = controller.updateData(dto);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void deleteData_shouldReturnTrue() {
        when(blbbTemplateService.deleteData(Arrays.asList("1"))).thenReturn(true);
        CommonResult<Boolean> result = controller.deleteData(Arrays.asList("1"));
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertTrue(result.getData());
    }

    @Test
    void getAllTemplates_shouldReturnList() {
        List<BlbbTemplateVO> list = Arrays.asList(new BlbbTemplateVO());
        when(blbbTemplateService.getAllTemplates()).thenReturn(list);
        CommonResult<List<BlbbTemplateVO>> result = controller.getAllTemplates();
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(list, result.getData());
    }

    @Test
    void getTemplateByType_shouldReturnDO() {
        BlbbTemplateDO data = new BlbbTemplateDO();
        when(blbbTemplateService.getTemplateByType("A")).thenReturn(data);
        CommonResult<BlbbTemplateDO> result = controller.getTemplateByType("A");
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(data, result.getData());
    }

    @Test
    void pageList_shouldReturnPage() {
        BlbbTemplatePageDTO pageDTO = new BlbbTemplatePageDTO();
        IPage<BlbbTemplateDO> page = mock(IPage.class);
        when(blbbTemplateService.pageList(pageDTO)).thenReturn(page);
        CommonResult<IPage<BlbbTemplateDO>> result = controller.pageList(pageDTO);
        Assertions.assertEquals(2000, result.getCode());
        Assertions.assertEquals(page, result.getData());
    }
}
