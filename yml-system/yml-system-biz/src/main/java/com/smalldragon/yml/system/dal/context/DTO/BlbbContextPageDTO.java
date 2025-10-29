package com.smalldragon.yml.system.dal.context.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:05
 **/
@Data
public class BlbbContextPageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关键词搜索", example = "air")
    private String keywords;

    @ApiModelProperty(value = "父级上下文ID", example = "1")
    private Long parentId;

    @ApiModelProperty(value = "节点层级", example = "2")
    private Integer nodeLevel;

    @ApiModelProperty(value = "是否有配置", example = "true")
    private Boolean hasConfig;
}
