package com.smalldragon.yml.system.dal.resource;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/18 22:42
 **/
@Data
@ApiModel(value = "角色资源表", description = "存放角色与资源之间的关系")
@TableName("sys_role_resource")
public class SysRoleResourceDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="存放资源ID",example = "1")
    private String resourceId;

    @ApiModelProperty(value="角色ID",example = "1")
    private String roleId;

}
