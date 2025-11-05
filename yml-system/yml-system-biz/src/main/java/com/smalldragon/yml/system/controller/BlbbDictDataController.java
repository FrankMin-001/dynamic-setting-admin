package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dictdata.BlbbDictDataDO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataCreateDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataPageDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataUpdateDTO;
import com.smalldragon.yml.system.dal.dictdata.VO.BlbbDictDataVO;
import com.smalldragon.yml.system.service.dictdata.BlbbDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 12:15
 **/
@Api(tags = "字典数据管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dictData")
public class BlbbDictDataController {

    private final BlbbDictDataService blbbDictDataService;

    @ApiOperation("添加字典数据")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbDictDataCreateDTO", value = "字典数据创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbDictDataCreateDTO createDTO) {
        return CommonResult.ok(blbbDictDataService.insertData(createDTO));
    }

    @ApiOperation("修改字典数据")
    @PostMapping("updateData")
    @ApiParam(name = "id", value = "字典数据ID", required = true)
    public CommonResult<Boolean> updateData(@RequestParam("id") String id, @RequestBody @Validated BlbbDictDataUpdateDTO updateDTO) {
        return CommonResult.ok(blbbDictDataService.updateData(id, updateDTO));
    }

    @ApiOperation("批量删除字典数据")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<String> ids) {
        return CommonResult.ok(blbbDictDataService.deleteData(ids));
    }

    @ApiOperation("获取字典数据信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的字典数据ID", required = true)
    public CommonResult<BlbbDictDataVO> getInfoById(@RequestParam("id") String id) {
        return CommonResult.ok(blbbDictDataService.getInfoById(id));
    }

    @ApiOperation("字典数据分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbDictDataPageDTO", value = "字典数据分页查询类", required = true)
    public CommonResult<IPage<BlbbDictDataDO>> pageList(@RequestBody @Validated BlbbDictDataPageDTO pageDTO) {
        return CommonResult.ok(blbbDictDataService.pageList(pageDTO));
    }

    @ApiOperation("根据字典类型查询数据")
    @GetMapping("listByDictType")
    @ApiParam(name = "dictType", value = "字典类型", required = true)
    public CommonResult<List<BlbbDictDataVO>> listByDictType(@RequestParam("dictType") String dictType) {
        return CommonResult.ok(blbbDictDataService.listByDictType(dictType));
    }

    @ApiOperation("启用/禁用字典数据")
    @PostMapping("toggleStatus")
    public CommonResult<Boolean> toggleStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        return CommonResult.ok(blbbDictDataService.toggleStatus(id, status));
    }
}


