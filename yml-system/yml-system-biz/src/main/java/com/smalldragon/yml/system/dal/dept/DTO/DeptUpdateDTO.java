package com.smalldragon.yml.system.dal.dept.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/6 20:57
 **/
@Data
public class DeptUpdateDTO implements Serializable {

    @ApiModelProperty(value="要修改的部门ID",example = "1",required = true)
    private String id;

    @ApiModelProperty(value="部门名称",example = "一级部门",required = true)
    private String deptName;

    @ApiModelProperty(value="父节点名称",example = "1")
    private String parentId;

}
