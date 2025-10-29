package com.smalldragon.yml.system.dal.template.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:35
 **/
@Data
public class BlbbTemplateCreateDTO implements Serializable {

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule", required = true)
    @NotBlank(message = "模版类型不能为空!")
    private String templateType;

    @ApiModelProperty(value = "模板名称", example = "升级规则模板", required = true)
    @NotBlank(message = "模板名称不能为空!")
    private String templateName;

    @ApiModelProperty(value = "列定义，JSON格式", example = "{\"columns\":[{\"name\":\"level\",\"type\":\"string\"}]}", required = true)
    @NotBlank(message = "列定义不能为空!")
    private String columnDefinitions;
}
