package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author YML
 * @Date 2025/1/15 12:38
 **/
@Api(tags = "配置版本历史")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/versionHistory")
public class BlbbVersionHistoryController {

    private final BlbbVersionHistoryService blbbVersionHistoryService;

    @ApiOperation("分页查询版本历史")
    @GetMapping("pageList")
    public CommonResult<IPage<BlbbVersionHistoryDO>> pageList(@RequestParam("pageNo") int pageNo,
                                                              @RequestParam("pageSize") int pageSize,
                                                              @RequestParam(value = "configDataId", required = false) Long configDataId) {
        return CommonResult.ok(blbbVersionHistoryService.pageList(pageNo, pageSize, configDataId));
    }
}


