package com.smalldragon.yml.system.service.role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.resource.VO.RoleResourceSelectVO;
import com.smalldragon.yml.system.dal.role.DTO.AssignDTO;
import com.smalldragon.yml.system.dal.role.DTO.RoleCreateDTO;
import com.smalldragon.yml.system.dal.role.DTO.RolePageDTO;
import com.smalldragon.yml.system.dal.role.DTO.RoleUpdateDTO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import com.smalldragon.yml.system.dal.role.VO.RoleDictVO;

import java.util.List;

/**
 * @author YML
 */
public interface RoleService {
    /**
     * @Description 创建角色
     * @Author YML
     * @Date 2025/3/20
     */
    Boolean insertData(RoleCreateDTO createDTO);

    /**
     * @Description 修改角色
     * @Author YML
     * @Date 2025/3/20
     */
    Boolean updateData(RoleUpdateDTO updateDTO);

    /**
     * @Description 批量删除角色
     * @Author YML
     * @Date 2025/3/20
     */
    Boolean deleteData(List<String> ids);

    /**
     * @Description 角色分页查询
     * @Author YML
     * @Date 2025/3/20
     */
    IPage<SysRoleDO> pageList(RolePageDTO pageDTO);

    /**
     * @Description 获取角色字典
     * @Author YML
     * @Date 2025/3/20
     */
    List<RoleDictVO> getRoleDict();

    /**
     * @Description 为角色赋予资源
     * @Author YML
     * @Date 2025/3/20
     */
    Boolean assign(AssignDTO assignDTO);

    /**
     * @Description 为角色取消资源
     * @Author YML
     * @Date 2025/3/20
     */
    Boolean unAssign(AssignDTO assignDTO);


    /**
     * @Description 根据角色查询所拥有的菜单资源和功能权限资源
     * @Author YML
     * @Date 2025/3/28
     */
    RoleResourceSelectVO getResourceByRole(String roleNumber);
}
