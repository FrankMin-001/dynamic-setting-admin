package com.smalldragon.yml.function.dal.SimpleChat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Author YML
 * @Date 2025/4/3 22:53
 **/
@Data
public class MessageDTO {

    @ApiModelProperty(value="房间号",example = "admin",required = true)
    @Length(min = 5,max = 12,message = "房间号长度在5-12位之间")
    private String roomId;

    @ApiModelProperty(value="消息内容",example = "你好!",required = true)
    @NotEmpty(message = "消息不能为空!")
    private String message;

}
