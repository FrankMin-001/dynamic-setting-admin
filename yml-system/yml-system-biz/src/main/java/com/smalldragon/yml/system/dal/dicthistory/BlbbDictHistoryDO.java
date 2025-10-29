package com.smalldragon.yml.system.dal.dicthistory;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:20
 **/
@Data
@ApiModel(value = "blbb_dict_history", description = "字典历史实体类")
@TableName("blbb_dict_history")
public class BlbbDictHistoryDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "字典类型", example = "booking_class")
    private String dictType;

    @ApiModelProperty(value = "操作类型：ADD/UPDATE/DELETE", example = "ADD")
    private String operationType;

    @ApiModelProperty(value = "旧数据(JSON)")
    private String oldData;

    @ApiModelProperty(value = "新数据(JSON)")
    private String newData;

    @ApiModelProperty(value = "操作人", example = "admin")
    private String operatedBy;
}


