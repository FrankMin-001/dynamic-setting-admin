package com.smalldragon.yml.system.dal.configdata;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:55
 **/
@Data
@ApiModel(value = "blbb_config_data", description = "配置数据实体类")
@TableName("blbb_config_data")
public class BlbbConfigDataDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

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
}
