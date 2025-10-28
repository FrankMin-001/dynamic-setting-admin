package com.smalldragon.yml.system.dal.role.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/5/14 23:07
 **/
@Data
public class DeptRoleStrDTO implements Serializable {


    @ApiModelProperty(value="所拥有角色")
    private String roleNumberStr;

    @ApiModelProperty(value="所拥有部门权限等级")
    private String deptLevelStr;

}
