package com.smalldragon.yml.system.dal.dept.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/6 20:56
 **/
@Data
public class DeptCreateDTO implements Serializable {

    @ApiModelProperty(value="部门名称",example = "一级部门",required = true)
    private String deptName;

    @ApiModelProperty(value="父节点名称(调用接口可选择,不传则默认为1级节点)",example = "1")
    private String parentId;

}
