package com.smalldragon.yml.context.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @Author YML
 * @Date 2025/3/16 23:15
 **/
@Data
public class LoginDTO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户名称")
    private String username;

}
