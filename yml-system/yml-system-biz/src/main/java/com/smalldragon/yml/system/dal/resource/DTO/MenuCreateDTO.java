package com.smalldragon.yml.system.dal.resource.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/22 16:37
 **/
@Data
public class MenuCreateDTO implements Serializable {

    @ApiModelProperty(value="菜单路径为英文和数字的混合(不带/)",example = "index")
    @NotNull(message = "菜单路径不能为空!")
    private String path;

    @ApiModelProperty(value="菜单名称",example = "首页")
    @NotNull(message = "菜单名称不能为空!")
    private String detail;

    @ApiModelProperty(value="排序字段(0-127的整数)",example = "127")
    @NotNull(message = "排序字段为0-127的整数,且不能为空!")
    @Min(value = 0,message = "最小的排序数字为0")
    @Max(value=127,message = "最大的排序数字为127")
    private Integer sort;

}
