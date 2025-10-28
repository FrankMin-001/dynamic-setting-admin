package com.smalldragon.yml.system.mapper.resource;

import com.github.yulichang.base.MPJBaseMapper;
import com.smalldragon.yml.system.dal.resource.SysRoleResourceDO;
import com.smalldragon.yml.system.dal.role.DTO.AssignDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author YML
 */
@Mapper
public interface RoleResourceMapper extends MPJBaseMapper<SysRoleResourceDO> {

    ///**
    // * @Description 为角色批量赋予权限
    // * @Author YML
    // * @Date 2025/3/20
    // */
    //void insertByBatch(@Param("assignList") List<SysRoleResourceDO> assignList);

    /**
     * @Description 为角色取消权限
     * @Author YML
     * @Date 2025/3/20
     */
    void deleteByBatch(@Param("assignDTO") AssignDTO assignDTO);

}
