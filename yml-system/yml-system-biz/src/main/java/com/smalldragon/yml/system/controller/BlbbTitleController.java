package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.title.BlbbTitleDO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleCreateDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitlePageDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleUpdateDTO;
import com.smalldragon.yml.system.dal.title.VO.BlbbTitleVO;
import com.smalldragon.yml.system.service.title.BlbbTitleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 13:00
 **/
@Api(tags = "动态配置标题管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/title")
public class BlbbTitleController {

    private final BlbbTitleService blbbTitleService;

    @ApiOperation("添加标题")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbTitleCreateDTO", value = "标题创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbTitleCreateDTO createDTO) {
        return CommonResult.ok(blbbTitleService.insertData(createDTO));
    }

    @ApiOperation("修改标题")
    @PostMapping("updateData")
    @ApiParam(name = "BlbbTitleUpdateDTO", value = "标题修改对象", required = true)
    public CommonResult<Boolean> updateData(@RequestBody @Validated BlbbTitleUpdateDTO updateDTO) {
        return CommonResult.ok(blbbTitleService.updateData(updateDTO));
    }

    @ApiOperation("批量删除标题")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<Long> ids) {
        return CommonResult.ok(blbbTitleService.deleteData(ids));
    }

    @ApiOperation("获取标题信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的标题ID", required = true)
    public CommonResult<BlbbTitleVO> getInfoById(@RequestParam("id") Long id) {
        return CommonResult.ok(blbbTitleService.getInfoById(id));
    }

    @ApiOperation("标题分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbTitlePageDTO", value = "标题分页查询类", required = true)
    public CommonResult<IPage<BlbbTitleDO>> pageList(@RequestBody @Validated BlbbTitlePageDTO pageDTO) {
        return CommonResult.ok(blbbTitleService.pageList(pageDTO));
    }

    @ApiOperation("根据上下文查询标题列表")
    @GetMapping("listByContextId")
    @ApiParam(name = "contextId", value = "上下文ID", required = true)
    public CommonResult<List<BlbbTitleVO>> listByContextId(@RequestParam("contextId") Long contextId) {
        return CommonResult.ok(blbbTitleService.listByContextId(contextId));
    }

    @ApiOperation("根据标题键名查询")
    @GetMapping("getByTitleKey")
    @ApiParam(name = "titleKey", value = "标题键名", required = true)
    public CommonResult<BlbbTitleDO> getByTitleKey(@RequestParam("titleKey") String titleKey) {
        return CommonResult.ok(blbbTitleService.getByTitleKey(titleKey));
    }
}


