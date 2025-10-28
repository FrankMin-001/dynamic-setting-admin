package com.smalldragon.yml.system.controller;

import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.auth.UserLoginDTO;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;
import com.smalldragon.yml.system.service.user.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author YML
 * @Date 2025/3/12 14:12
 **/
@Api(tags = "用户登录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/auth")
public class AuthLoginController {

    private final AuthService authService;


    @ApiOperation("用户登录")
    @PostMapping("login")
    @ApiParam(name = "UserLoginDTO",value = "用户登录并获取TOKEN信息(有效期2小时)",required = true)
    public CommonResult<LoginVO> insertData(@RequestBody @Validated UserLoginDTO loginDTO) {
        return CommonResult.ok(authService.authLogin(loginDTO));
    }

    @ApiOperation("用户注销登录")
    @GetMapping("logout")
    public CommonResult<Boolean> logout() {
        return CommonResult.ok(authService.logout());
    }

    @ApiOperation("获取登录用户信息")
    @GetMapping("getLoginInfo")
    public CommonResult<LoginVO> getLoginInfo() {
        return CommonResult.ok(authService.getLoginInfo());
    }

    @ApiOperation("获取验证码")
    @PostMapping("getCaptchaCode")
    public CommonResult<String> getCaptchaCode() {
        return CommonResult.ok(authService.getCaptchaCode());
    }

}
