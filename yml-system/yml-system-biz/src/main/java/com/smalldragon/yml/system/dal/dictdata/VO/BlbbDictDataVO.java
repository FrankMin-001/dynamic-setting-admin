package com.smalldragon.yml.system.dal.dictdata.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 12:05
 **/
@Data
@ApiModel(value = "BlbbDictDataVO", description = "字典数据视图对象")
public class BlbbDictDataVO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "字典类型", example = "booking_class")
    private String dictType;

    @ApiModelProperty(value = "字典编码", example = "C")
    private String dictCode;

    @ApiModelProperty(value = "字典值", example = "C舱")
    private String dictValue;

    @ApiModelProperty(value = "显示顺序", example = "10")
    private Integer displayOrder;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @ApiModelProperty(value = "扩展属性(JSON)", example = "")
    private String extendProps;

    @ApiModelProperty(value = "创建人", example = "admin")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedTime;
}


