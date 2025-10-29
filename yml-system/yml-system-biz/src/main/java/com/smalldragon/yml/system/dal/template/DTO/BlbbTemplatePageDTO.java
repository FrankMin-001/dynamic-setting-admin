package com.smalldragon.yml.system.dal.template.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:35
 **/
@Data
public class BlbbTemplatePageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关键词搜索", example = "upgrade")
    private String keywords;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule")
    private String templateType;
}
