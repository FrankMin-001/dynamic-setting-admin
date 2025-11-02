package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.configdata.BlbbConfigDataDO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataCreateDTO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataPageDTO;
import com.smalldragon.yml.system.dal.configdata.VO.BlbbConfigDataVO;
import com.smalldragon.yml.system.service.configdata.BlbbConfigDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 11:10
 **/
@Api(tags = "动态配置数据管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/configData")
public class BlbbConfigDataController {

    private final BlbbConfigDataService blbbConfigDataService;

    @ApiOperation("添加配置数据")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbConfigDataCreateDTO", value = "配置数据创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbConfigDataCreateDTO createDTO) {
        return CommonResult.ok(blbbConfigDataService.insertData(createDTO));
    }

    @ApiOperation("修改配置数据")
    @PostMapping("updateData")
    @ApiParam(name = "id", value = "配置数据ID", required = true)
    public CommonResult<Boolean> updateData(@RequestParam("id") String id,
                                           @RequestParam("rowData") String rowData,
                                           @RequestParam(value = "changeDescription", required = false) String changeDescription) {
        return CommonResult.ok(blbbConfigDataService.updateData(id, rowData, changeDescription));
    }

    @ApiOperation("批量删除配置数据")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<String> ids) {
        return CommonResult.ok(blbbConfigDataService.deleteData(ids));
    }

    @ApiOperation("获取配置数据信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的配置数据ID", required = true)
    public CommonResult<BlbbConfigDataVO> getInfoById(@RequestParam("id") String id) {
        return CommonResult.ok(blbbConfigDataService.getInfoById(id));
    }

    @ApiOperation("配置数据分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbConfigDataPageDTO", value = "配置数据分页查询类", required = true)
    public CommonResult<IPage<BlbbConfigDataDO>> pageList(@RequestBody @Validated BlbbConfigDataPageDTO pageDTO) {
        return CommonResult.ok(blbbConfigDataService.pageList(pageDTO));
    }

    @ApiOperation("根据模板类型获取配置数据列表")
    @GetMapping("getConfigDataByTemplateType")
    @ApiParam(name = "templateType", value = "模板类型", required = true)
    public CommonResult<List<BlbbConfigDataVO>> getConfigDataByTemplateType(@RequestParam("templateType") String templateType) {
        return CommonResult.ok(blbbConfigDataService.getConfigDataByTemplateType(templateType));
    }

    @ApiOperation("根据标题ID获取配置数据列表")
    @GetMapping("getConfigDataByTitleId")
    @ApiParam(name = "titleId", value = "标题ID", required = true)
    public CommonResult<List<BlbbConfigDataVO>> getConfigDataByTitleId(@RequestParam("titleId") String titleId) {
        return CommonResult.ok(blbbConfigDataService.getConfigDataByTitleId(titleId));
    }

    @ApiOperation("激活/禁用配置数据")
    @PostMapping("toggleActive")
    @ApiParam(name = "id", value = "配置数据ID", required = true)
    public CommonResult<Boolean> toggleActive(@RequestParam("id") String id, @RequestParam("isActive") Boolean isActive) {
        return CommonResult.ok(blbbConfigDataService.toggleActive(id, isActive));
    }
}
