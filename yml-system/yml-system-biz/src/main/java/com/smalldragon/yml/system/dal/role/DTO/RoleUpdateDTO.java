package com.smalldragon.yml.system.dal.role.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/20 11:43
 **/
@Data
public class RoleUpdateDTO implements Serializable {

    @ApiModelProperty(value="主键ID",example = "1")
    @NotNull(message = "主键ID不能为空!!")
    private String id;

    @ApiModelProperty(value="角色名称",example = "管理员")
    @NotNull(message = "角色名称不能为空!")
    private String roleName;

}
