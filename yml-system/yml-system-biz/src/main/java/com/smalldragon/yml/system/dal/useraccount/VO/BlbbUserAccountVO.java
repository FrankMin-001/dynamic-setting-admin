package com.smalldragon.yml.system.dal.useraccount.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 11:40
 **/
@Data
@ApiModel(value = "BlbbUserAccountVO", description = "用户账号视图对象")
public class BlbbUserAccountVO implements Serializable {

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}
