package com.smalldragon.yml.system.dal.useraccount.DTO;

import com.smalldragon.yml.pojo.QueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/1/15 11:35
 **/
@Data
public class BlbbUserAccountPageDTO extends QueryPage implements Serializable {

    @ApiModelProperty(value = "关键词搜索", example = "admin")
    private String keywords;
}
