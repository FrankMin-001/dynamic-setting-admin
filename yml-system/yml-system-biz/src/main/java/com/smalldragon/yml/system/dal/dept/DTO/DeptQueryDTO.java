package com.smalldragon.yml.system.dal.dept.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/6 22:09
 **/
@Data
public class DeptQueryDTO implements Serializable {

    @ApiModelProperty(value="模糊查询",example = "某部门名称")
    private String keywords;

}
