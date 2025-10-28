package com.smalldragon.yml.system.dal.dept;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/5/14 22:06
 **/
@Data
@TableName("sys_dept")
public class RoleDeptDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="部门ID",example = "1")
    private String deptId;

    @ApiModelProperty(value="角色ID",example = "1")
    private String roleId;

}
