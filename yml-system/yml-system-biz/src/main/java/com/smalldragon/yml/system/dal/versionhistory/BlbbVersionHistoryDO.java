package com.smalldragon.yml.system.dal.versionhistory;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/1/15 12:30
 **/
@Data
@ApiModel(value = "blbb_version_history", description = "配置版本历史实体类")
@TableName("blbb_version_history")
public class BlbbVersionHistoryDO implements Serializable {

    @TableId
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "配置数据ID", example = "1001")
    private String configDataId;

    @ApiModelProperty(value = "旧版本号", example = "1")
    private String oldVersion;

    @ApiModelProperty(value = "新版本号", example = "2")
    private String newVersion;

    @ApiModelProperty(value = "变更类型：CREATE/UPDATE/DELETE", example = "UPDATE")
    private String changeType;

    @ApiModelProperty(value = "变更描述")
    private String changeDescription;

    @ApiModelProperty(value = "变更数据快照(JSON)")
    private String changeData;

    @ApiModelProperty(value = "操作人", example = "admin")
    private String operatedBy;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime operatedTime;
}


