package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.role.DTO.RoleCreateDTO;
import com.smalldragon.yml.system.dal.role.DTO.RolePageDTO;
import com.smalldragon.yml.system.dal.role.DTO.RoleUpdateDTO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import com.smalldragon.yml.system.dal.role.VO.RoleDictVO;
import com.smalldragon.yml.system.service.role.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/18 22:24
 **/
@Api(tags = "系统角色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/role")
public class SysRoleController {

    private final RoleService roleService;

    @ApiOperation("添加角色")
    @PostMapping("insertData")
    @ApiParam(name = "RoleCreateDTO",value = "用户创建角色",required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated RoleCreateDTO createDTO) {
        return CommonResult.ok(roleService.insertData(createDTO));
    }

    @ApiOperation("修改角色")
    @PostMapping("updateData")
    @ApiParam(name = "RoleUpdateDTO",value = "用户修改角色",required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated RoleUpdateDTO updateDTO) {
        return CommonResult.ok(roleService.updateData(updateDTO));
    }

    @ApiOperation("批量删除角色")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids",value = "要输出的主键ID集合",required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<String> ids) {
        return CommonResult.ok(roleService.deleteData(ids));
    }

    @ApiOperation("角色分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "RolePageDTO",value = "角色分页查询",required = true)
    public CommonResult<IPage<SysRoleDO>> pageList(@RequestBody @Validated RolePageDTO pageDTO) {
        return CommonResult.ok(roleService.pageList(pageDTO));
    }

    @ApiOperation("获取角色字典")
    @GetMapping("getRoleDict")
    public CommonResult<List<RoleDictVO>> getRoleDict() {
        return CommonResult.ok(roleService.getRoleDict());
    }


}
