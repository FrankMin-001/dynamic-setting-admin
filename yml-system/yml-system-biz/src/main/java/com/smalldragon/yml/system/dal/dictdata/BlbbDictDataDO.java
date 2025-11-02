package com.smalldragon.yml.system.dal.dictdata;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:00
 **/
@Data
@ApiModel(value = "blbb_dict_data", description = "字典数据实体类")
@TableName("blbb_dict_data")
public class BlbbDictDataDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

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
}
