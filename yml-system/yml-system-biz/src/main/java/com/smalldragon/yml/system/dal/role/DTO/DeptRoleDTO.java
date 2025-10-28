package com.smalldragon.yml.system.dal.role.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/5/14 23:02
 **/
@Data
public class DeptRoleDTO implements Serializable {

    @ApiModelProperty(value="角色编码",example = "admin")
    private String roleNumber;

    @ApiModelProperty(value="部门权限等级",example = "1")
    private String deptLevel;



}
