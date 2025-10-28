package com.smalldragon.yml.system.dal.role.VO;

import com.smalldragon.yml.system.dal.resource.VO.ResourceVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/25 15:08
 **/
@Data
public class RoleResourceVO implements Serializable {

    private String roleNumber;

    private ResourceVO resourceData;

    public ResourceVO getResourceData() {
        if (resourceData == null){
            return new ResourceVO();
        }
        return resourceData;
    }

}
