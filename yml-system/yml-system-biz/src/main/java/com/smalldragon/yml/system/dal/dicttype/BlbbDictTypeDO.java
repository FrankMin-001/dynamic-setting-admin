package com.smalldragon.yml.system.dal.dicttype;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:15
 **/
@Data
@ApiModel(value = "blbb_dict_type", description = "字典类型实体类")
@TableName("blbb_dict_type")
public class BlbbDictTypeDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "字典类型，如：booking_class, service_class, discount_type", example = "booking_class")
    private String dictType;

    @ApiModelProperty(value = "字典名称，如：预订舱位类型、服务等级类型、折扣类型", example = "预订舱位类型")
    private String dictName;

    @ApiModelProperty(value = "字典描述", example = "用于管理预订舱位类型的字典")
    private String description;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @ApiModelProperty(value = "创建人", example = "admin")
    private String createdBy;
}
