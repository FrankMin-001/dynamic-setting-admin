package com.smalldragon.yml.system.dal.template.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 10:40
 **/
@Data
@ApiModel(value = "BlbbTemplateVO", description = "模板定义视图对象")
public class BlbbTemplateVO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule")
    private String templateType;

    @ApiModelProperty(value = "模板名称", example = "升级规则模板")
    private String templateName;

    @ApiModelProperty(value = "列定义，JSON格式", example = "{\"columns\":[{\"name\":\"level\",\"type\":\"string\"}]}")
    private String columnDefinitions;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime updatedTime;
}
