package com.smalldragon.yml.system.service.resource.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smalldragon.yml.enums.ResourceTypeEnum;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.system.dal.resource.DTO.MenuCreateDTO;
import com.smalldragon.yml.system.dal.resource.DTO.MenuUpdateDTO;
import com.smalldragon.yml.system.dal.resource.SysResourceDO;
import com.smalldragon.yml.system.dal.resource.SysRoleResourceDO;
import com.smalldragon.yml.system.mapper.resource.ResourceMapper;
import com.smalldragon.yml.system.mapper.resource.RoleResourceMapper;
import com.smalldragon.yml.system.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/23 8:46
 **/
@Slf4j
@RequiredArgsConstructor
@Service("ResourceServiceImpl")
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;

    private final CommonService commonService;

    private final RoleResourceMapper roleResourceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMenu(MenuCreateDTO createDTO) {
        String path = createDTO.getPath();
        // 检验菜单路径的唯一性
        checkPath(path);

        SysResourceDO sysResourceDO = BeanUtil.copyProperties(createDTO, SysResourceDO.class);
        sysResourceDO.setType(ResourceTypeEnum.MENU.getCode());
        resourceMapper.insert(sysResourceDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(MenuUpdateDTO updateDTO) {
        SysResourceDO sysResourceDO = resourceMapper.selectById(updateDTO.getId());
        if (sysResourceDO == null) {
            throw new RuntimeException("要修改的菜单不存在!");
        }

        BeanUtil.copyProperties(updateDTO, sysResourceDO);
        resourceMapper.updateById(sysResourceDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMenu(List<String> ids) {
        // 删除菜单时删除所对应的所有拥有该权限的中间表数据
        LambdaQueryWrapper<SysRoleResourceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysRoleResourceDO::getResourceId,ids);
        roleResourceMapper.delete(lambdaQueryWrapper);

        // 删除资源表中的数据
        resourceMapper.deleteBatchIds(ids);

        // 刷新缓存
        commonService.refreshUserTokenSession();

        return true;
    }

    private void checkPath(String path) {
        LambdaQueryWrapper<SysResourceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysResourceDO::getPath,path);
        SysResourceDO sysResourceDO = resourceMapper.selectOne(lambdaQueryWrapper);
        if (sysResourceDO != null) {
            throw new RuntimeException("菜单路径已重复,请重新输入!");
        }
    }

}
