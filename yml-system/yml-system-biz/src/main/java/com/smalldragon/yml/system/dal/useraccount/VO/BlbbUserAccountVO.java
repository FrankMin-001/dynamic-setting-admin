package com.smalldragon.yml.system.dal.useraccount.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String id;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createdTime;
}
