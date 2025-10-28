package com.smalldragon.yml.system.dal.role.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/20 11:42
 **/
@Data
public class RoleCreateDTO implements Serializable {

    @ApiModelProperty(value="角色名称",example = "管理员")
    @NotNull(message = "角色名称不能为空!")
    private String roleName;

    @ApiModelProperty(value="角色编号(唯一)",example = "admin")
    @NotNull(message = "角色编号不能为空!")
    private String roleNumber;

}
