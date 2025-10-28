package com.smalldragon.yml.system.dal.resource.VO;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YML
 */
@Data
public class ResourceVO implements Serializable {

    @ApiModelProperty(value="菜单权限")
    private String[] menuList;

    @ApiModelProperty(value="功能权限")
    private String[] functionList;

    @ApiModelProperty(value="菜单权限")
    private String menuStr;

    @ApiModelProperty(value="功能权限")
    private String functionStr;

    public String getMenuStr() {
        if (StrUtil.isEmpty(menuStr)){
            return "";
        }
        return menuStr;
    }

    public String getFunctionStr() {
        if (StrUtil.isEmpty(functionStr)){
            return "";
        }
        return functionStr;
    }

    public String[] getMenuList() {
        if (StrUtil.isNotEmpty(menuStr)){
            menuList = menuStr.split(",");
        }
        return menuList;
    }

    public String[] getFunctionList() {
        if (StrUtil.isNotEmpty(functionStr)){
            functionList = functionStr.split(",");
        }
        return functionList;
    }

}
