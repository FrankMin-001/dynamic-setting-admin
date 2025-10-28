package com.smalldragon.yml.system.dal.resource.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/28 11:11
 **/
@Data
public class RoleResourceSelectVO implements Serializable {

    @ApiModelProperty(value="角色编码")
    private String roleNumber;

    @ApiModelProperty(value="菜单资源")
    private List<ResourceTypeVO> menuData;

    @ApiModelProperty(value="功能资源")
    private List<ResourceTypeVO> functionData;

}
