package com.smalldragon.yml.system.dal.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smalldragon.yml.core.DataObject.BaseDO;
import com.smalldragon.yml.enums.SexyEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/1 1:34
 **/
@Data
@ApiModel(value = "user", description = "用户实体类")
@TableName("sys_user")
public class UserDO extends BaseDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="用户账号名称",example = "梁朝伟")
    private String username;

    @ApiModelProperty(value="用户密码",example = "6-16位组合的数字.英文,特殊符号")
    private String password;

    @ApiModelProperty(value="性别",example = "0:男 1:女")
    private Integer sex;

    @ApiModelProperty(value="性别名称")
    @TableField(exist = false)
    private String sexName;

    @ApiModelProperty(value="部门ID",example = "1")
    private String deptId;

    public String getSexName() {
        if (sex != null) {
            return SexyEnum.getValueByCode(sex);
        }
        return sexName;
    }
}
