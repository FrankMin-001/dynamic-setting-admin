package com.smalldragon.yml.function.dal.SimpleChat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @Author YML
 * @Date 2025/4/3 23:21
 **/
@Data
public class MessageQueryDTO {

    @NotEmpty(message = "房间号不能为空!")
    @ApiParam(name = "roomId", value = "房间号ID", required = true)
    String roomId;

    @ApiParam(name = "count", value = "要查询的消息数量", required = true)
    @Min(value = 10, message = "最小为10")
    Integer count;

}

