package com.smalldragon.yml.system.dal.user.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/2 1:43
 **/
@Data
public class UserCreateDTO implements Serializable {

    @ApiModelProperty(value="用户账号名称",example = "梁朝伟",required = true)
    @Length(min = 5,max = 12,message = "账号长度在5-12位之间")
    private String username;

    @ApiModelProperty(value="用户密码",example = "6-16位组合的数字.英文,特殊符号",required = true)
    @Length(min = 6,message = "密码长度最小长度为6")
    private String password;

    @ApiModelProperty(value="性别",example = "0:男 1:女",required = true)
    @NotNull(message = "性别字段不能为空!")
    @Min(value = 0,message = "0:男 1:女")
    @Max(value=1,message = "0:男 1:女")
    private Integer sex;

    @ApiModelProperty(value="部门ID",example = "1",required = true)
    @NotNull(message = "部门ID不能为空!")
    private String deptId;

    @ApiModelProperty(value="传角色ID集合",example = "1,2,3,4",required = true)
    @NotEmpty(message = "角色至少选择一项!")
    private List<String> roleIds;

}
