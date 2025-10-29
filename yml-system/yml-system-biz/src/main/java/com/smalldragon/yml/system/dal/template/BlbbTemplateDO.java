package com.smalldragon.yml.system.dal.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:35
 **/
@Data
@ApiModel(value = "blbb_template", description = "模板定义实体类")
@TableName("blbb_template")
public class BlbbTemplateDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule")
    private String templateType;

    @ApiModelProperty(value = "模板名称", example = "升级规则模板")
    private String templateName;

    @ApiModelProperty(value = "列定义，JSON格式", example = "{\"columns\":[{\"name\":\"level\",\"type\":\"string\"}]}")
    private String columnDefinitions;
}
