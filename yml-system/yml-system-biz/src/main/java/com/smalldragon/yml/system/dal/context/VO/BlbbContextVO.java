package com.smalldragon.yml.system.dal.context.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 10:10
 **/
@Data
@ApiModel(value = "BlbbContextVO", description = "上下文视图对象")
public class BlbbContextVO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "父级上下文ID", example = "0")
    private Long parentId;

    @ApiModelProperty(value = "完整路径", example = "/admin/spend/air")
    private String contextPath;

    @ApiModelProperty(value = "节点名称", example = "air")
    private String nodeName;

    @ApiModelProperty(value = "节点层级", example = "2")
    private Integer nodeLevel;

    @ApiModelProperty(value = "描述信息", example = "航空消费升级规则配置")
    private String description;

    @ApiModelProperty(value = "是否有配置", example = "true")
    private Boolean hasConfig;

    @ApiModelProperty(value = "创建人", example = "admin")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "子节点列表")
    private List<BlbbContextVO> children;
}
