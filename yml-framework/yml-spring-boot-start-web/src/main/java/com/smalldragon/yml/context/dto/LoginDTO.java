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

    @ApiModelProperty(value = "性别(0:男 1:女)")
    private Integer sex;

    @ApiModelProperty(value = "性别名称")
    private String sexName;

    @ApiModelProperty(value="部门ID")
    private String deptId;

    @ApiModelProperty(value="用户所拥有的角色")
    private String roles;

    @ApiModelProperty(value="该用户所拥有的部门数据权限集合")
    private String deptLevels;

    @ApiModelProperty(value="该用户所拥有的菜单权限")
    private Set<String> menuData;

    @ApiModelProperty(value="该用户所拥有的功能权限")
    private Set<String> functionData;

    @ApiModelProperty(value="放在Header请求头中的名称")
    private String authTokenName;

    @ApiModelProperty(value="登录所用TOKEN")
    private String authTokenCode;

}
