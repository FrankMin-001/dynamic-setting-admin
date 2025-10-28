package com.smalldragon.yml.system.service.role.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.constants.CommonConstants;
import com.smalldragon.yml.enums.ResourceTypeEnum;
import com.smalldragon.yml.system.dal.role.DTO.AssignDTO;
import com.smalldragon.yml.system.dal.role.DTO.RoleCreateDTO;
import com.smalldragon.yml.system.dal.role.DTO.RolePageDTO;
import com.smalldragon.yml.system.dal.role.DTO.RoleUpdateDTO;
import com.smalldragon.yml.system.dal.user.UserRoleDO;
import com.smalldragon.yml.system.mapper.resource.ResourceMapper;
import com.smalldragon.yml.system.mapper.resource.RoleResourceMapper;
import com.smalldragon.yml.system.mapper.role.RoleMapper;
import com.smalldragon.yml.system.mapper.user.UserRoleMapper;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.util.RedisUtil;
import com.smalldragon.yml.system.dal.resource.SysResourceDO;
import com.smalldragon.yml.system.dal.resource.SysRoleResourceDO;
import com.smalldragon.yml.system.dal.resource.VO.ResourceTypeVO;
import com.smalldragon.yml.system.dal.resource.VO.RoleResourceSelectVO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import com.smalldragon.yml.system.dal.role.VO.RoleDictVO;
import com.smalldragon.yml.system.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/3/18 23:24
 **/
@Slf4j
@RequiredArgsConstructor
@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final RoleResourceMapper roleResourceMapper;

    private final RedisUtil redisUtil;

    private final ResourceMapper resourceMapper;

    private final CommonService commonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(RoleCreateDTO createDTO) {
        String roleNumber = createDTO.getRoleNumber();
        checkRoleNumber(roleNumber);
        SysRoleDO sysRoleDO = BeanUtil.copyProperties(createDTO, SysRoleDO.class);
        roleMapper.insert(sysRoleDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(RoleUpdateDTO updateDTO) {
        SysRoleDO original = roleMapper.selectById(updateDTO.getId());

        if (original == null) {
            throw new RuntimeException("要修改的数据不存在!");
        }

        SysRoleDO sysRoleDO = BeanUtil.copyProperties(updateDTO, SysRoleDO.class);
        roleMapper.updateById(sysRoleDO);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<String> ids) {

        // 删除角色表中数据
        roleMapper.deleteByIds(ids);

        // 删除资源角色中间表中的数据
        LambdaQueryWrapper<SysRoleResourceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysRoleResourceDO::getRoleId, ids);
        roleResourceMapper.delete(lambdaQueryWrapper);

        // 删除角色用户中间表中的数据
        LambdaQueryWrapper<UserRoleDO> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.in(UserRoleDO::getRoleId, ids);
        userRoleMapper.delete(userRoleLambdaQueryWrapper);


        // 刷新缓存(用户缓存以及角色权限缓存)
        commonService.refreshUserTokenSession();

        return true;
    }

    @Override
    public IPage<SysRoleDO> pageList(RolePageDTO pageDTO) {
        IPage<SysRoleDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<SysRoleDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(SysRoleDO::getRoleName, pageDTO.getKeywords()).or()
                .like(SysRoleDO::getRoleNumber, pageDTO.getKeywords());
        lambdaQueryWrapper.orderByDesc(SysRoleDO::getId);
        return roleMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public List<RoleDictVO> getRoleDict() {
        List<SysRoleDO> list = roleMapper.selectList(null);
        return BeanUtil.copyToList(list, RoleDictVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assign(AssignDTO assignDTO) {
        String roleId = assignDTO.getRoleId();
        // 对角色进行校验
        checkRole(roleId);

        List<SysRoleResourceDO> assignList = getAssignList(assignDTO);
        for (SysRoleResourceDO sysRoleResourceDO : assignList) {
            roleResourceMapper.insert(sysRoleResourceDO);
        }
        // 刷新缓存
        commonService.refreshUserTokenSession();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unAssign(AssignDTO assignDTO) {
        String roleId = assignDTO.getRoleId();

        // 对角色进行校验
        checkRole(roleId);
        roleResourceMapper.deleteByBatch(assignDTO);

        // 刷新缓存
        commonService.refreshUserTokenSession();
        return true;
    }

    @Override
    public RoleResourceSelectVO getResourceByRole(String roleNumber) {

        if (roleNumber.equals(CommonConstants.ADMIN)) {
            throw new RuntimeException("管理员admin默认拥有所有权限!!!");
        }

        RoleResourceSelectVO roleResourceSelectVO = new RoleResourceSelectVO();

        // 校验角色是否存在
        LambdaQueryWrapper<SysRoleDO> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(SysRoleDO::getRoleNumber, roleNumber);
        Long count = roleMapper.selectCount(roleLambdaQueryWrapper);

        if (count == 0) {
            throw new RuntimeException("要查询的角色不存在!");
        }

        // 设置角色
        roleResourceSelectVO.setRoleNumber(roleNumber);

        // 校验缓存是否存在
        if (!redisUtil.hasKey(CommonConstants.ROLE_PERMISSION_CACHE)) {
            commonService.refreshRoleResource();
        }

        // 菜单权限
        List<ResourceTypeVO> menuPermissionData = getPermissionDataListByRole(roleNumber, ResourceTypeEnum.MENU.getCode());
        roleResourceSelectVO.setMenuData(menuPermissionData);

        // 功能权限
        List<ResourceTypeVO> functionPermissionData = getPermissionDataListByRole(roleNumber, ResourceTypeEnum.FUNCTION.getCode());
        roleResourceSelectVO.setFunctionData(functionPermissionData);

        return roleResourceSelectVO;
    }

    private List<ResourceTypeVO> getPermissionDataListByRole(String roleNumber,String resourceType) {
        LambdaQueryWrapper<SysResourceDO> resourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resourceLambdaQueryWrapper.eq(SysResourceDO::getType,resourceType);
        List<SysResourceDO> allMenuData = resourceMapper.selectList(resourceLambdaQueryWrapper);
        List<ResourceTypeVO> allMenuDataVO = BeanUtil.copyToList(allMenuData, ResourceTypeVO.class);

        if (roleNumber == null) {
            return allMenuDataVO;
        } else {
            LambdaQueryWrapper<SysRoleDO> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleLambdaQueryWrapper.eq(SysRoleDO::getRoleNumber, roleNumber);
            SysRoleDO sysRoleDO = roleMapper.selectOne(roleLambdaQueryWrapper);

            // 根据角色查询该角色所拥有的权限
            LambdaQueryWrapper<SysRoleResourceDO> roleResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleResourceLambdaQueryWrapper.eq(SysRoleResourceDO::getRoleId, sysRoleDO.getId());
            List<SysRoleResourceDO> sysRoleResourceDOList = roleResourceMapper.selectList(roleResourceLambdaQueryWrapper);
            if (CollectionUtils.isEmpty(sysRoleResourceDOList)) {
                return allMenuDataVO;
            } else {
                // 提取所有 resourceId 到 HashSet
                List<String> resourceIds = sysRoleResourceDOList.stream().map(SysRoleResourceDO::getResourceId).collect(Collectors.toList());
                allMenuDataVO.forEach(vo -> vo.setOwned(resourceIds.contains(vo.getId())));
            }
            return allMenuDataVO;
        }
    }

    private void checkRole(String roleId) {
        // 校验角色是否存在
        LambdaQueryWrapper<SysRoleDO> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(SysRoleDO::getId, roleId);
        SysRoleDO sysRoleDO = roleMapper.selectOne(roleLambdaQueryWrapper);
        if (sysRoleDO.getRoleNumber().equals(CommonConstants.ADMIN)) {
            throw new RuntimeException("管理员角色默认拥有所有权限无法操控!");
        }
    }

    private List<SysRoleResourceDO> getAssignList(AssignDTO assignDTO) {
        String roleId = assignDTO.getRoleId();
        List<String> resourceIds = assignDTO.getResourceIds();
        return resourceIds.stream().map(item -> {
            SysRoleResourceDO sysRoleResource = new SysRoleResourceDO();
            sysRoleResource.setResourceId(item);
            sysRoleResource.setRoleId(roleId);
            return sysRoleResource;
        }).collect(Collectors.toList());
    }

    private void checkRoleNumber(String roleNumber) {
        LambdaQueryWrapper<SysRoleDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleDO::getRoleNumber, roleNumber);
        SysRoleDO sysRoleDO = roleMapper.selectOne(lambdaQueryWrapper);
        if (sysRoleDO != null) {
            throw new RuntimeException("角色编码重复,请重新输入!");
        }
    }

}
