package com.smalldragon.yml.function.controller;

import com.smalldragon.yml.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 飞机大战游戏控制器
 * @author YML
 */
@Api(tags = "飞机大战游戏")
@Controller
@RequestMapping("/game/plane")
public class PlaneGameController {

    /**
     * 返回游戏页面
     */
    @GetMapping("/play")
    @ApiOperation("游戏页面")
    public String gamePage() {
        return "plane-game";
    }

    /**
     * 保存游戏分数
     */
    @PostMapping("/score")
    @ResponseBody
    @ApiOperation("保存游戏分数")
    public CommonResult<String> saveScore(@RequestParam String playerName, @RequestParam Integer score) {
        // 简单返回成功，暂不持久化到数据库
        return CommonResult.success("分数已保存: " + playerName + " - " + score);
    }

    /**
     * 获取排行榜（前10名）
     */
    @GetMapping("/leaderboard")
    @ResponseBody
    @ApiOperation("获取排行榜")
    public CommonResult<String> getLeaderboard() {
        // 暂时返回空数据
        return CommonResult.success("排行榜功能待实现");
    }
}
