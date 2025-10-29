package com.smalldragon.yml.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.pojo.CommonResult;
import com.smalldragon.yml.system.dal.dicthistory.BlbbDictHistoryDO;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author YML
 * @Date 2025/1/15 12:28
 **/
@Api(tags = "字典历史记录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dictHistory")
public class BlbbDictHistoryController {

    private final BlbbDictHistoryService blbbDictHistoryService;

    @ApiOperation("分页查询字典历史")
    @GetMapping("pageList")
    public CommonResult<IPage<BlbbDictHistoryDO>> pageList(@RequestParam("pageNo") int pageNo,
                                                           @RequestParam("pageSize") int pageSize,
                                                           @RequestParam(value = "dictType", required = false) String dictType) {
        return CommonResult.ok(blbbDictHistoryService.pageList(pageNo, pageSize, dictType));
    }
}


