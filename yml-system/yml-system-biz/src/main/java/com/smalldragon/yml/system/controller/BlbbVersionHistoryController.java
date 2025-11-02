package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                                                              @RequestParam(value = "configDataId", required = false) String configDataId,
                                                              @RequestParam(value = "configDataIds", required = false) String configDataIds) {
        // 如果传入了configDataIds（逗号分隔的字符串），使用批量查询接口
        if (configDataIds != null && !configDataIds.trim().isEmpty()) {
            List<String> idList = Arrays.stream(configDataIds.split(","))
                    .map(String::trim)
                    .filter(id -> !id.isEmpty())
                    .collect(Collectors.toList());
            if (!idList.isEmpty()) {
                return CommonResult.ok(blbbVersionHistoryService.pageListByConfigDataIds(pageNo, pageSize, idList));
            }
        }
        // 否则使用单个configDataId查询
        return CommonResult.ok(blbbVersionHistoryService.pageList(pageNo, pageSize, configDataId));
    }
}


