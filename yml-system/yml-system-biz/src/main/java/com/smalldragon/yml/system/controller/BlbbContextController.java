package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.context.BlbbContextDO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextCreateDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextPageDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextUpdateDTO;
import com.smalldragon.yml.system.dal.context.VO.BlbbContextVO;
import com.smalldragon.yml.system.service.context.BlbbContextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 10:30
 **/
@Api(tags = "动态配置上下文管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/context")
public class BlbbContextController {

    private final BlbbContextService blbbContextService;

    @ApiOperation("添加上下文")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbContextCreateDTO", value = "上下文创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbContextCreateDTO createDTO) {
        return CommonResult.ok(blbbContextService.insertData(createDTO));
    }

    @ApiOperation("修改上下文")
    @PostMapping("updateData")
    @ApiParam(name = "BlbbContextUpdateDTO", value = "上下文修改对象", required = true)
    public CommonResult<Boolean> updateData(@RequestBody @Validated BlbbContextUpdateDTO updateDTO) {
        return CommonResult.ok(blbbContextService.updateData(updateDTO));
    }

    @ApiOperation("批量删除上下文")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<Long> ids) {
        return CommonResult.ok(blbbContextService.deleteData(ids));
    }

    @ApiOperation("获取上下文信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的上下文ID", required = true)
    public CommonResult<BlbbContextVO> getInfoById(@RequestParam("id") Long id) {
        return CommonResult.ok(blbbContextService.getInfoById(id));
    }

    @ApiOperation("上下文分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbContextPageDTO", value = "上下文分页查询类", required = true)
    public CommonResult<IPage<BlbbContextDO>> pageList(@RequestBody @Validated BlbbContextPageDTO pageDTO) {
        return CommonResult.ok(blbbContextService.pageList(pageDTO));
    }

    @ApiOperation("获取上下文树形结构")
    @GetMapping("getContextTree")
    public CommonResult<List<BlbbContextVO>> getContextTree() {
        return CommonResult.ok(blbbContextService.getContextTree());
    }

    @ApiOperation("根据路径获取上下文")
    @GetMapping("getContextByPath")
    @ApiParam(name = "contextPath", value = "上下文路径", required = true)
    public CommonResult<BlbbContextDO> getContextByPath(@RequestParam("contextPath") String contextPath) {
        return CommonResult.ok(blbbContextService.getContextByPath(contextPath));
    }
}
