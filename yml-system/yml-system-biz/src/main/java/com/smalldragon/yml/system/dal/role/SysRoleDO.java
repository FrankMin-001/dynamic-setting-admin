package com.smalldragon.yml.system.dal.role;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/18 22:25
 **/
@Data
@ApiModel(value = "role", description = "角色实体类")
@TableName("sys_role")
public class SysRoleDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="角色名称",example = "管理员")
    private String roleName;

    @ApiModelProperty(value="部门权限等级",example = "1")
    private Integer deptLevel;

    @ApiModelProperty(value="角色编号(唯一)",example = "admin")
    private String roleNumber;

}
