package com.smalldragon.yml.system.mapper.role;

import com.github.yulichang.base.MPJBaseMapper;
import com.smalldragon.yml.system.dal.role.DTO.DeptRoleDTO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import com.smalldragon.yml.system.dal.role.VO.RoleResourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YML
 */
@Mapper
public interface RoleMapper extends MPJBaseMapper<SysRoleDO> {


    void deleteByIds(@Param("ids") List<String> ids);

    /**
     * @Description 根据角色来查询每个角色所拥有的权限资源
     * @Author YML
     * @Date 2025/3/25
     */
    List<RoleResourceVO>  getRoleResourceList();

    List<DeptRoleDTO> getRolesByUserId(@Param("userId") String userId);
}
