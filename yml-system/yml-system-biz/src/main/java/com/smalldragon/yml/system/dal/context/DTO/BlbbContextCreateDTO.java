package com.smalldragon.yml.system.dal.context.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:05
 **/
@Data
public class BlbbContextCreateDTO implements Serializable {

    @ApiModelProperty(value = "父级上下文ID，用于构建层级结构", example = "0")
    private String parentId;

    @ApiModelProperty(value = "完整路径，如：/admin/spend/air", example = "/admin/spend/air", required = true)
    @NotBlank(message = "上下文路径不能为空!")
    private String contextPath;

    @ApiModelProperty(value = "节点名称，如：admin, spend, air", example = "air", required = true)
    @NotBlank(message = "节点名称不能为空!")
    private String nodeName;

    @ApiModelProperty(value = "节点层级，根节点为0，逐级递增", example = "2", required = true)
    @NotNull(message = "节点层级不能为空!")
    private Integer nodeLevel;

    @ApiModelProperty(value = "描述信息", example = "航空消费升级规则配置")
    private String description;

    @ApiModelProperty(value = "该节点是否有具体配置", example = "true")
    private Boolean hasConfig;
}
