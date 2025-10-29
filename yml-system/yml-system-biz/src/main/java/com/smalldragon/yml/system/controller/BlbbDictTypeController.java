package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicttype.BlbbDictTypeDO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypeCreateDTO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypePageDTO;
import com.smalldragon.yml.system.dal.dicttype.VO.BlbbDictTypeVO;
import com.smalldragon.yml.system.service.dicttype.BlbbDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 11:30
 **/
@Api(tags = "字典类型管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dictType")
public class BlbbDictTypeController {

    private final BlbbDictTypeService blbbDictTypeService;

    @ApiOperation("添加字典类型")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbDictTypeCreateDTO", value = "字典类型创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbDictTypeCreateDTO createDTO) {
        return CommonResult.ok(blbbDictTypeService.insertData(createDTO));
    }

    @ApiOperation("修改字典类型")
    @PostMapping("updateData")
    @ApiParam(name = "id", value = "字典类型ID", required = true)
    public CommonResult<Boolean> updateData(@RequestParam("id") Long id, @RequestBody @Validated BlbbDictTypeCreateDTO updateDTO) {
        return CommonResult.ok(blbbDictTypeService.updateData(id, updateDTO));
    }

    @ApiOperation("批量删除字典类型")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<Long> ids) {
        return CommonResult.ok(blbbDictTypeService.deleteData(ids));
    }

    @ApiOperation("获取字典类型信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的字典类型ID", required = true)
    public CommonResult<BlbbDictTypeVO> getInfoById(@RequestParam("id") Long id) {
        return CommonResult.ok(blbbDictTypeService.getInfoById(id));
    }

    @ApiOperation("字典类型分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbDictTypePageDTO", value = "字典类型分页查询类", required = true)
    public CommonResult<IPage<BlbbDictTypeDO>> pageList(@RequestBody @Validated BlbbDictTypePageDTO pageDTO) {
        return CommonResult.ok(blbbDictTypeService.pageList(pageDTO));
    }

    @ApiOperation("根据字典类型获取字典类型信息")
    @GetMapping("getDictTypeByType")
    @ApiParam(name = "dictType", value = "字典类型", required = true)
    public CommonResult<BlbbDictTypeDO> getDictTypeByType(@RequestParam("dictType") String dictType) {
        return CommonResult.ok(blbbDictTypeService.getDictTypeByType(dictType));
    }

    @ApiOperation("获取所有启用的字典类型列表")
    @GetMapping("getAllEnabledDictTypes")
    public CommonResult<List<BlbbDictTypeVO>> getAllEnabledDictTypes() {
        return CommonResult.ok(blbbDictTypeService.getAllEnabledDictTypes());
    }

    @ApiOperation("启用/禁用字典类型")
    @PostMapping("toggleStatus")
    @ApiParam(name = "id", value = "字典类型ID", required = true)
    public CommonResult<Boolean> toggleStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        return CommonResult.ok(blbbDictTypeService.toggleStatus(id, status));
    }
}
