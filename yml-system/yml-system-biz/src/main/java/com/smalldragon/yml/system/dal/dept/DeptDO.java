package com.smalldragon.yml.system.dal.dept;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smalldragon.yml.core.DataObject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/6 20:51
 **/
@Data
@TableName("sys_dept")
public class DeptDO  extends BaseDO implements Serializable {

    @TableId
    private String id;

    @ApiModelProperty(value="部门名称",example = "一级部门")
    private String deptName;

    @ApiModelProperty(value="父节点名称",example = "1")
    private String parentId;



}
