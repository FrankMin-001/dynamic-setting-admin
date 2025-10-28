package com.smalldragon.yml.system.dal.role.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/20 16:30
 **/
@Data
public class AssignDTO implements Serializable {

    @ApiModelProperty(value="角色主键ID",example = "1")
    @NotNull(message = "角色主键ID不能为空!")
    private String roleId;

    @ApiModelProperty(value="资源主键ID",example = "1")
    @NotNull(message = "资源主键ID不能为空!")
    @Size(min = 2, message = "资源主键ID数量必须大于1")
    private List<String> resourceIds;

}
