package com.smalldragon.yml.function.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.smalldragon.yml.function.service.SignInService;
import com.smalldragon.yml.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author YML
 * @Date 2025/4/12 16:52
 **/
@Api(tags = "并发签到")
@RestController
@RequiredArgsConstructor
@RequestMapping("/function/signIn")
public class SignInController {

    private final SignInService signInService;

    @ApiOperation("进行签到")
    @GetMapping("doing")
    @SaCheckPermission("function:signIn:doing")
    public CommonResult<Boolean> createSimpleChat() {
        signInService.doingByQueue();
        return CommonResult.ok(true);
    }

}
