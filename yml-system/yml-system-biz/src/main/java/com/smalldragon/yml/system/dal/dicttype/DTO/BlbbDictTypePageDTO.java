package com.smalldragon.yml.system.dal.dicttype.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:15
 **/
@Data
public class BlbbDictTypePageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关键词搜索", example = "booking")
    private String keywords;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
}
