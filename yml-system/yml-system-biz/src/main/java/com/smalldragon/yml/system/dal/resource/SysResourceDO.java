package com.smalldragon.yml.system.dal.resource;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/22 16:23
 **/
@Data
@ApiModel(value = "资源表", description = "存放菜单资源以及接口资源")
@TableName("sys_resource")
public class SysResourceDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="资源路径" ,example = "菜单就是菜单编码,接口自动扫描的填充")
    private String path;

    @ApiModelProperty(value="中文名称(菜单即菜单名称,功能则为功能名称)",example = "首页/系统管理-添加数据")
    private String detail;

    @ApiModelProperty(value="功能类型(menu:菜单类型 function:功能)",example = "menu")
    private String type;

    @ApiModelProperty(value="0-127的纯整数(0为最优先级)",example = "0")
    private Integer sort;

}
