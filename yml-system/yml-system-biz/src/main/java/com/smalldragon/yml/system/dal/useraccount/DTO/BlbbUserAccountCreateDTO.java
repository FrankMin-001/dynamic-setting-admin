package com.smalldragon.yml.system.dal.useraccount.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:35
 **/
@Data
public class BlbbUserAccountCreateDTO implements Serializable {

    @ApiModelProperty(value = "用户名", example = "admin", required = true)
    @NotBlank(message = "用户名不能为空!")
    @Size(min = 3, max = 20, message = "用户名长度在3-20位之间")
    private String username;

    @ApiModelProperty(value = "密码（将使用BCrypt哈希加密存储）", example = "123456", required = true)
    @NotBlank(message = "密码不能为空!")
    @Size(min = 6, max = 20, message = "密码长度在6-20位之间")
    private String password;
}
