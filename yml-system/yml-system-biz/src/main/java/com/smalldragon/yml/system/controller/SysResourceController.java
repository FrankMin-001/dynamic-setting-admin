package com.smalldragon.yml.system.controller;

import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.resource.DTO.MenuCreateDTO;
import com.smalldragon.yml.system.dal.resource.DTO.MenuUpdateDTO;
import com.smalldragon.yml.system.dal.resource.VO.RoleResourceSelectVO;
import com.smalldragon.yml.system.dal.role.DTO.AssignDTO;
import com.smalldragon.yml.system.service.permission.PermissionService;
import com.smalldragon.yml.system.service.resource.ResourceService;
import com.smalldragon.yml.system.service.role.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/22 23:56
 **/
@Api(tags = "资源管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/resource")
public class SysResourceController {

    private final PermissionService permissionService;

    private final ResourceService resourceService;

    private final RoleService roleService;

    @ApiOperation("扫描权限接口")
    @PostMapping("scanning")
    public CommonResult<Boolean> scanning() {
        return CommonResult.ok(permissionService.scanning());
    }

    @ApiOperation("添加菜单")
    @PostMapping("addMenu")
    @ApiParam(name = "MenuCreateDTO",value = "添加菜单DTO类",required = true)
    public CommonResult<Boolean> addMenu(@RequestBody @Validated MenuCreateDTO createDTO) {
        return CommonResult.ok(resourceService.addMenu(createDTO));
    }

    @ApiOperation("修改菜单")
    @PostMapping("updateMenu")
    @ApiParam(name = "MenuUpdateDTO",value = "修改菜单DTO类",required = true)
    public CommonResult<Boolean> addMenu(@RequestBody @Validated MenuUpdateDTO updateDTO) {
        return CommonResult.ok(resourceService.updateMenu(updateDTO));
    }

    @ApiOperation("删除菜单")
    @PostMapping("deleteMenu")
    @ApiParam(name = "ids",value = "要删除的菜单ids",required = true)
    public CommonResult<Boolean> deleteMenu(@RequestBody List<String> ids) {
        return CommonResult.ok(resourceService.deleteMenu(ids));
    }

    @ApiOperation("为角色赋予权限")
    @PostMapping("assign")
    @ApiParam(name = "AssignDTO",value = "要赋予的角色赋予资源DTO类",required = true)
    public CommonResult<Boolean> assign(@RequestBody @Validated AssignDTO assignDTO) {
        return CommonResult.ok(roleService.assign(assignDTO));
    }

    @ApiOperation("为角色取消权限")
    @PostMapping("unAssign")
    @ApiParam(name = "AssignDTO",value = "要取消的角色赋予资源DTO类",required = true)
    public CommonResult<Boolean> pageList(@RequestBody @Validated AssignDTO assignDTO) {
        return CommonResult.ok(roleService.unAssign(assignDTO));
    }

    @ApiOperation("根据角色查询所拥有的菜单资源和功能权限资源")
    @GetMapping("getResourceByRole")
    public CommonResult<RoleResourceSelectVO> getResourceByRole(@RequestParam("roleNumber") @NotEmpty(message = "角色编码不能为空!") String roleNumber) {
        return CommonResult.ok(roleService.getResourceByRole(roleNumber));
    }

}
