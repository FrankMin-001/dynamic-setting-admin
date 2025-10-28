package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;
import com.smalldragon.yml.system.dal.user.DTO.PasswordDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserCreateDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserPageDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserUpdateDTO;
import com.smalldragon.yml.system.dal.user.UserDO;
import com.smalldragon.yml.system.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/2 16:22
 **/
@Api(tags = "系统用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user")
public class SysUserController {

    private final UserService userService;

    @ApiOperation("添加用户")
    @PostMapping("insertData")
    @ApiParam(name = "userCreateDTO",value = "用户创建对象",required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated UserCreateDTO createDTO) {
        return CommonResult.ok(userService.insertData(createDTO));
    }

    @ApiOperation("修改用户")
    @PostMapping("updateData")
    @ApiParam(name = "updateUserDTO",value = "用户修改对象",required = true)
    public CommonResult<Boolean> updateData(@RequestBody @Validated UserUpdateDTO updateDTO) {
        return CommonResult.ok(userService.updateData(updateDTO));
    }

    @ApiOperation("修改密码")
    @PostMapping("changePassword")
    @ApiParam(name = "PasswordDTO",value = "用户修改密码",required = true)
    public CommonResult<Boolean> changePassword(@RequestBody @Validated PasswordDTO passwordDTO) {
        return CommonResult.ok(userService.changePassword(passwordDTO));
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids",value = "要输出的主键ID集合",required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<String> ids) {
        return CommonResult.ok(userService.deleteData(ids));
    }

    @ApiOperation("获取用户信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id",value = "要查询的用户ID",required = true)
    public CommonResult<LoginVO> getInfoById(@RequestParam("id") String id) {
        return CommonResult.ok(userService.getInfoById(id));
    }

    @ApiOperation("用户分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "UserPageDTO",value = "用户分页查询类",required = true)
    public CommonResult<IPage<UserDO>> pageList(@RequestBody @Validated UserPageDTO pageDTO) {
        return CommonResult.ok(userService.pageList(pageDTO));
    }

}
