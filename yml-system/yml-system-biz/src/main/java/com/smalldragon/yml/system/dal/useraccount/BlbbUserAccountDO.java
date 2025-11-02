package com.smalldragon.yml.system.dal.useraccount;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:35
 **/
@Data
@ApiModel(value = "blbb_user_account", description = "用户账号实体类")
@TableName("blbb_user_account")
public class BlbbUserAccountDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "用户ID", example = "1")
    private String id;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "密码（加密存储）", example = "encrypted_password")
    private String password;
}
