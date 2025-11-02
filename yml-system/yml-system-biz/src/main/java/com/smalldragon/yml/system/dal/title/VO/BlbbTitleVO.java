package com.smalldragon.yml.system.dal.title.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 12:57
 **/
@Data
@ApiModel(value = "BlbbTitleVO", description = "标题视图对象")
public class BlbbTitleVO implements Serializable {

    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "上下文ID", example = "100")
    private String contextId;

    @ApiModelProperty(value = "标题名称", example = "Booking Classes")
    private String titleName;

    @ApiModelProperty(value = "标题键名", example = "booking_classes")
    private String titleKey;

    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer displayOrder;

    @ApiModelProperty(value = "模板ID", example = "10")
    private Long templateId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime updatedTime;

}


