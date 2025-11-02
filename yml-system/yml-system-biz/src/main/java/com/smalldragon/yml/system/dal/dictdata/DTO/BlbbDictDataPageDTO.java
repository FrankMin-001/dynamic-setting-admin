package com.smalldragon.yml.system.dal.dictdata.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 12:00
 **/
@Data
@Accessors(chain = false)
public class BlbbDictDataPageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "字典类型", example = "booking_class")
    private String dictType;

    @ApiModelProperty(value = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
}


