package com.smalldragon.yml.system.dal.title.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:55
 **/
@Data
public class BlbbTitleUpdateDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1", required = true)
    @NotNull(message = "ID不能为空!")
    private Long id;

    @ApiModelProperty(value = "上下文ID", example = "100", required = true)
    @NotNull(message = "上下文ID不能为空!")
    private Long contextId;

    @ApiModelProperty(value = "标题名称", example = "Booking Classes", required = true)
    @NotBlank(message = "标题名称不能为空!")
    private String titleName;

    @ApiModelProperty(value = "标题键名", example = "booking_classes", required = true)
    @NotBlank(message = "标题键名不能为空!")
    private String titleKey;

    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer displayOrder;

    @ApiModelProperty(value = "模板ID", example = "10", required = true)
    @NotNull(message = "模板ID不能为空!")
    private Long templateId;
}


