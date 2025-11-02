package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.template.BlbbTemplateDO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateCreateDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplatePageDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateUpdateDTO;
import com.smalldragon.yml.system.dal.template.VO.BlbbTemplateVO;
import com.smalldragon.yml.system.service.template.BlbbTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 10:50
 **/
@Api(tags = "动态配置模板管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/template")
public class BlbbTemplateController {

    private final BlbbTemplateService blbbTemplateService;

    @ApiOperation("添加模板")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbTemplateCreateDTO", value = "模板创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbTemplateCreateDTO createDTO) {
        return CommonResult.ok(blbbTemplateService.insertData(createDTO));
    }

    @ApiOperation("修改模板")
    @PostMapping("updateData")
    @ApiParam(name = "BlbbTemplateUpdateDTO", value = "模板修改对象", required = true)
    public CommonResult<Boolean> updateData(@RequestBody @Validated BlbbTemplateUpdateDTO updateDTO) {
        return CommonResult.ok(blbbTemplateService.updateData(updateDTO));
    }

    @ApiOperation("批量删除模板")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<String> ids) {
        return CommonResult.ok(blbbTemplateService.deleteData(ids));
    }

    @ApiOperation("获取模板信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的模板ID", required = true)
    public CommonResult<BlbbTemplateVO> getInfoById(@RequestParam("id") String id) {
        return CommonResult.ok(blbbTemplateService.getInfoById(id));
    }

    @ApiOperation("模板分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbTemplatePageDTO", value = "模板分页查询类", required = true)
    public CommonResult<IPage<BlbbTemplateDO>> pageList(@RequestBody @Validated BlbbTemplatePageDTO pageDTO) {
        return CommonResult.ok(blbbTemplateService.pageList(pageDTO));
    }

    @ApiOperation("根据模板类型获取模板")
    @GetMapping("getTemplateByType")
    @ApiParam(name = "templateType", value = "模板类型", required = true)
    public CommonResult<BlbbTemplateDO> getTemplateByType(@RequestParam("templateType") String templateType) {
        return CommonResult.ok(blbbTemplateService.getTemplateByType(templateType));
    }

    @ApiOperation("获取所有模板列表")
    @GetMapping("getAllTemplates")
    public CommonResult<List<BlbbTemplateVO>> getAllTemplates() {
        return CommonResult.ok(blbbTemplateService.getAllTemplates());
    }
}
