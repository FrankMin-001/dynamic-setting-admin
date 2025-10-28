package com.smalldragon.yml.system.dal.resource.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author YML
 * @Date 2025/3/28 13:32
 **/
@Data
public class ResourceTypeVO {

    @ApiModelProperty(value="资源主键ID")
    private String id;

    @ApiModelProperty(value="权限编码")
    private String path;

    @ApiModelProperty(value="权限编码")
    private String detail;

    @ApiModelProperty(value="是否拥有其权限")
    private Boolean owned;

    public Boolean getOwned() {
        if (owned == null){
            return false;
        }
        return owned;
    }

}
