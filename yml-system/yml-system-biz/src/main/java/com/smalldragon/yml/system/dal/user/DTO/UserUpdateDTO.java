package com.smalldragon.yml.system.dal.user.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/2 1:46
 **/
@Data
public class UserUpdateDTO implements Serializable {

    @ApiModelProperty(value="用户ID",example = "1",required = true)
    @NotEmpty(message = "主键ID不能为空")
    private String id;

    @ApiModelProperty(value="性别",example = "0:男 1:女",required = true)
    @NotNull(message = "性别字段不能为空!")
    @Min(value = 0,message = "0:男 1:女")
    @Max(value=1,message = "0:男 1:女")
    private Integer sex;

    @ApiModelProperty(value="部门ID",example = "1",required = true)
    @NotNull(message = "部门ID不能为空!")
    private String deptId;

    @ApiModelProperty(value="传角色ID集合",example = "1,2,3,4",required = true)
    @NotEmpty(message = "角色至少选择一项!")
    private List<String> roleIds;

}
