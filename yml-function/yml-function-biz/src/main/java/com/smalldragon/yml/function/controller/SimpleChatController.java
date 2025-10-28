package com.smalldragon.yml.function.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.smalldragon.yml.function.dal.SimpleChat.MessageDTO;
import com.smalldragon.yml.function.dal.SimpleChat.MessageQueryDTO;
import com.smalldragon.yml.function.service.SimpleChatService;
import com.smalldragon.yml.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author YML
 * @Date 2025/4/3 15:27
 **/
@Api(tags = "简易临时聊天室")
@RestController
@RequiredArgsConstructor
@RequestMapping("/function/simpleChat")
public class SimpleChatController {

    private final SimpleChatService simpleChatService;

    @ApiOperation("创建临时聊天房间")
    @GetMapping("createRoom")
    @SaCheckPermission("function:simpleChat:createRoom")
    public CommonResult<Boolean> createSimpleChat() {
        return CommonResult.ok(simpleChatService.createSimpleChat());
    }

    @ApiOperation("关闭临时聊天房间")
    @GetMapping("closeRoom")
    @SaCheckPermission("function:simpleChat:closeRoom")
    public CommonResult<Boolean> closeSimpleChat() {
        return CommonResult.ok(simpleChatService.closeRoom());
    }

    @ApiOperation("查询聊天室是否存在")
    @GetMapping("isAlive")
    @SaCheckPermission("function:simpleChat:isAlive")
    public CommonResult<Boolean> isAlive(@RequestParam(value = "roomId",required = false) String roomId) {
        return CommonResult.ok(simpleChatService.isAlive(roomId));
    }

    @ApiOperation("为聊天室发送消息")
    @PostMapping("sendMessageByRoomId")
    @SaCheckPermission("function:simpleChat:sendMessageByRoomId")
    public CommonResult<Boolean> sendMessageByRoomId(@RequestBody @Validated MessageDTO messageDTO) {
        return CommonResult.ok(simpleChatService.sendMessageByRoomId(messageDTO));
    }

    @ApiOperation("查询最近消息(默认10条)")
    @PostMapping("getRecentMessage")
    @SaCheckPermission("function:simpleChat:getRecentMessage")
    public CommonResult<Map<String,String>> getRecentMessage(@RequestBody @Validated MessageQueryDTO queryDTO) {
        return CommonResult.ok(simpleChatService.getRecentMessage(queryDTO));
    }

}
