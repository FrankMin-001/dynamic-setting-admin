package com.smalldragon.yml.system.mapper.dept;

import com.github.yulichang.base.MPJBaseMapper;
import com.smalldragon.yml.system.dal.dept.DeptDO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/4/25 3:44
 **/
@Mapper
public interface DeptMapper extends MPJBaseMapper<DeptDO> {

    @Select("SELECT id FROM sys_dept")
    List<String> selectIdList();

    List<String> selectCustomDeptIds(List<String> roleList);
}
