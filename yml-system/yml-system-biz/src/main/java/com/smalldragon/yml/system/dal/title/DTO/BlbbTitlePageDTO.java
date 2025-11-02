package com.smalldragon.yml.system.dal.title.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:56
 **/
@Data
@Accessors(chain = false)
public class BlbbTitlePageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "上下文ID", example = "100")
    private String contextId;

    @ApiModelProperty(value = "模板ID", example = "10")
    private Long templateId;
}


