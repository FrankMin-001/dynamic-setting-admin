package com.smalldragon.yml.system.dal.user.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/12 16:06
 **/
@Data
public class PasswordDTO implements Serializable {

    @ApiModelProperty(value="用户账号名称",example = "梁朝伟123",required = true)
    @Length(min = 6,max = 12,message = "账号长度在6-12位之间")
    private String username;

    @ApiModelProperty(value="用户密码",example = "6-16位组合的数字.英文,特殊符号",required = true)
    @Length(min = 6,message = "密码长度最小长度为6")
    private String password;

}
