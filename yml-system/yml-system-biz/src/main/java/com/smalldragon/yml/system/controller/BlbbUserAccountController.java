package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountCreateDTO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountPageDTO;
import com.smalldragon.yml.system.dal.useraccount.VO.BlbbUserAccountVO;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 11:50
 **/
@Api(tags = "用户账号管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/userAccount")
public class BlbbUserAccountController {

    private final BlbbUserAccountService blbbUserAccountService;

    @ApiOperation("添加用户账号")
    @PostMapping("insertData")
    @ApiParam(name = "BlbbUserAccountCreateDTO", value = "用户账号创建对象", required = true)
    public CommonResult<Boolean> insertData(@RequestBody @Validated BlbbUserAccountCreateDTO createDTO) {
        return CommonResult.ok(blbbUserAccountService.insertData(createDTO));
    }

    @ApiOperation("修改用户密码（BCrypt哈希加密）")
    @PostMapping("updatePassword")
    @ApiParam(name = "id", value = "用户账号ID", required = true)
    public CommonResult<Boolean> updatePassword(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword) {
        return CommonResult.ok(blbbUserAccountService.updatePassword(id, newPassword));
    }

    @ApiOperation("批量删除用户账号")
    @DeleteMapping("deleteData")
    @ApiParam(name = "ids", value = "要删除的主键ID集合", required = true)
    public CommonResult<Boolean> deleteData(@RequestBody List<Long> ids) {
        return CommonResult.ok(blbbUserAccountService.deleteData(ids));
    }

    @ApiOperation("获取用户账号信息")
    @GetMapping("getInfoById")
    @ApiParam(name = "id", value = "要查询的用户账号ID", required = true)
    public CommonResult<BlbbUserAccountVO> getInfoById(@RequestParam("id") Long id) {
        return CommonResult.ok(blbbUserAccountService.getInfoById(id));
    }

    @ApiOperation("用户账号分页查询")
    @PostMapping("pageList")
    @ApiParam(name = "BlbbUserAccountPageDTO", value = "用户账号分页查询类", required = true)
    public CommonResult<IPage<BlbbUserAccountDO>> pageList(@RequestBody @Validated BlbbUserAccountPageDTO pageDTO) {
        return CommonResult.ok(blbbUserAccountService.pageList(pageDTO));
    }

    @ApiOperation("根据用户名获取用户账号")
    @GetMapping("getUserAccountByUsername")
    @ApiParam(name = "username", value = "用户名", required = true)
    public CommonResult<BlbbUserAccountDO> getUserAccountByUsername(@RequestParam("username") String username) {
        return CommonResult.ok(blbbUserAccountService.getUserAccountByUsername(username));
    }

    @ApiOperation("验证用户登录（BCrypt哈希验证）")
    @PostMapping("validateLogin")
    @ApiParam(name = "username", value = "用户名", required = true)
    public CommonResult<Boolean> validateLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        return CommonResult.ok(blbbUserAccountService.validateLogin(username, password));
    }
}
