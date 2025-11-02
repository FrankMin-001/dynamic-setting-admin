package com.smalldragon.yml.system.dal.title;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:50
 **/
@Data
@ApiModel(value = "blbb_title", description = "标题实体类")
@TableName("blbb_title")
public class BlbbTitleDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "上下文ID", example = "100")
    private String contextId;

    @ApiModelProperty(value = "标题名称", example = "Booking Classes")
    private String titleName;

    @ApiModelProperty(value = "标题键名(代码获取用)", example = "booking_classes")
    private String titleKey;

    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer displayOrder;

    @ApiModelProperty(value = "模板ID", example = "10")
    private String templateId;
}


