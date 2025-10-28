package com.smalldragon.yml.system.dal.role.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/20 16:13
 **/
@Data
public class RoleDictVO implements Serializable {

    @ApiModelProperty(value="角色名称",example = "管理员")
    private String roleName;

    @ApiModelProperty(value="角色编号(唯一)",example = "admin")
    private String roleNumber;

}
