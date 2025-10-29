package com.smalldragon.yml.system.dal.dicttype.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:15
 **/
@Data
public class BlbbDictTypeCreateDTO implements Serializable {

    @ApiModelProperty(value = "字典类型，如：booking_class, service_class, discount_type", example = "booking_class", required = true)
    @NotBlank(message = "字典类型不能为空!")
    private String dictType;

    @ApiModelProperty(value = "字典名称，如：预订舱位类型、服务等级类型、折扣类型", example = "预订舱位类型", required = true)
    @NotBlank(message = "字典名称不能为空!")
    private String dictName;

    @ApiModelProperty(value = "字典描述", example = "用于管理预订舱位类型的字典")
    private String description;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1", required = true)
    @NotNull(message = "状态不能为空!")
    private Integer status;
}
