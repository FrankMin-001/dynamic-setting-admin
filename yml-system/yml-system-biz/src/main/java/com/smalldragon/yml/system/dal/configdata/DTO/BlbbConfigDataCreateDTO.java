package com.smalldragon.yml.system.dal.configdata.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:55
 **/
@Data
public class BlbbConfigDataCreateDTO implements Serializable {

    @ApiModelProperty(value = "关联的标题ID", example = "1")
    private String titleId;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule", required = true)
    @NotBlank(message = "模版类型不能为空!")
    private String templateType;

    @ApiModelProperty(value = "行数据，按照模板定义的格式存储", example = "{\"level\":\"VIP\",\"discount\":0.8}", required = true)
    @NotBlank(message = "行数据不能为空!")
    private String rowData;

    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer displayOrder;

    @ApiModelProperty(value = "是否激活", example = "true")
    private Boolean isActive;

    @ApiModelProperty(value = "数据版本", example = "1")
    private Integer version;
}
