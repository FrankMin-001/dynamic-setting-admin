package com.smalldragon.yml.system.dal.configdata.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 11:00
 **/
@Data
@ApiModel(value = "BlbbConfigDataVO", description = "配置数据视图对象")
public class BlbbConfigDataVO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "关联的标题ID", example = "1")
    private String titleId;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule")
    private String templateType;

    @ApiModelProperty(value = "行数据，按照模板定义的格式存储", example = "{\"level\":\"VIP\",\"discount\":0.8}")
    private String rowData;

    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer displayOrder;

    @ApiModelProperty(value = "是否激活", example = "true")
    private Boolean isActive;

    @ApiModelProperty(value = "数据版本", example = "1")
    private Integer version;

    @ApiModelProperty(value = "创建人", example = "admin")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime updatedTime;
}
