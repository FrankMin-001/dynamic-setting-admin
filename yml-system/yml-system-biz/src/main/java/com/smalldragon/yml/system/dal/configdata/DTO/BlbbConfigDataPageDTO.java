package com.smalldragon.yml.system.dal.configdata.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 10:55
 **/
@Data
@Accessors(chain = false)
public class BlbbConfigDataPageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关联的标题ID", example = "1")
    private String titleId;

    @ApiModelProperty(value = "模版类型", example = "upgrade_rule")
    private String templateType;

    @ApiModelProperty(value = "是否激活", example = "true")
    private Boolean isActive;
}
