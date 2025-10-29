package com.smalldragon.yml.system.dal.context;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:00
 **/
@Data
@ApiModel(value = "blbb_context", description = "上下文实体类")
@TableName("blbb_context")
public class BlbbContextDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "父级上下文ID，用于构建层级结构", example = "0")
    private Long parentId;

    @ApiModelProperty(value = "完整路径，如：/admin/spend/air", example = "/admin/spend/air")
    private String contextPath;

    @ApiModelProperty(value = "节点名称，如：admin, spend, air", example = "air")
    private String nodeName;

    @ApiModelProperty(value = "节点层级，根节点为0，逐级递增", example = "2")
    private Integer nodeLevel;

    @ApiModelProperty(value = "描述信息", example = "航空消费升级规则配置")
    private String description;

    @ApiModelProperty(value = "该节点是否有具体配置", example = "true")
    private Boolean hasConfig;

    @ApiModelProperty(value = "创建人", example = "admin")
    private String createdBy;
}
