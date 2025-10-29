package com.smalldragon.yml.system.dal.dictdata.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:00
 **/
@Data
public class BlbbDictDataCreateDTO implements Serializable {

    @ApiModelProperty(value = "字典类型", example = "booking_class", required = true)
    @NotBlank(message = "字典类型不能为空!")
    private String dictType;

    @ApiModelProperty(value = "字典编码", example = "C", required = true)
    @NotBlank(message = "字典编码不能为空!")
    private String dictCode;

    @ApiModelProperty(value = "字典值", example = "C舱", required = true)
    @NotBlank(message = "字典值不能为空!")
    private String dictValue;

    @ApiModelProperty(value = "显示顺序", example = "10")
    private Integer displayOrder;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @ApiModelProperty(value = "扩展属性(JSON)", example = "{\\"color\\":\\"red\\"}")
    private String extendProps;
}


