package com.smalldragon.yml.system.dal.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/12 14:15
 **/
@Data
public class UserLoginDTO implements Serializable {

    @ApiModelProperty(value="用户账号名称",example = "admin",required = true)
    @Length(min = 5,max = 12,message = "账号长度在5-12位之间")
    private String username;

    //@ApiModelProperty(value="用户密码",example = "6-16位组合的数字.英文,特殊符号",required = true)
    @ApiModelProperty(value="用户密码",example = "tVPJshVR06Da3qU3hNnCgA==",required = true)
    @Length(min = 6,message = "密码长度最少6位!")
    private String password;

    @ApiModelProperty(value="图形验证码",example = "加上图片前缀查看后输入",required = true)
    @NotEmpty(message = "图形验证码不能为空!")
    private String captchaCode;

}
