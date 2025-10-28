package com.smalldragon.yml.system.dal.resource.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author YML
 */
@Data
public class ResourceSetVO implements Serializable {

    @ApiModelProperty(value="菜单权限")
    private Set<String> menuSet;

    @ApiModelProperty(value="功能权限")
    private Set<String> functionSet;

}
