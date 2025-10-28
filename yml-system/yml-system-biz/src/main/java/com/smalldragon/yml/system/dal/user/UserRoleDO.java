package com.smalldragon.yml.system.dal.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author YML
 * @Date 2025/3/26 12:09
 **/
@Data
@ApiModel(value = "UserRoleDO", description = "用户角色联系表")
@TableName("sys_user_role")
public class UserRoleDO {

    @TableId
    private String id;

    @ApiModelProperty(value="角色ID")
    private String roleId;

    @ApiModelProperty(value="角色ID")
    private String userId;

}
