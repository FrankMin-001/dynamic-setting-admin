package com.smalldragon.yml.system.dal.title.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:56
 **/
@Data
public class BlbbTitlePageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关键词搜索(名称/键名)", example = "booking")
    private String keywords;

    @ApiModelProperty(value = "上下文ID", example = "100")
    private Long contextId;

    @ApiModelProperty(value = "模板ID", example = "10")
    private Long templateId;
}


