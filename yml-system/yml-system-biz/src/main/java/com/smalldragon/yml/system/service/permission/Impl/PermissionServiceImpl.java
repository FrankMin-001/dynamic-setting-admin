package com.smalldragon.yml.system.service.permission.Impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smalldragon.yml.enums.ResourceTypeEnum;
import com.smalldragon.yml.system.dal.resource.SysResourceDO;
import com.smalldragon.yml.system.dal.resource.SysRoleResourceDO;
import com.smalldragon.yml.system.dal.role.SysRoleDO;
import com.smalldragon.yml.system.mapper.resource.ResourceMapper;
import com.smalldragon.yml.system.mapper.resource.RoleResourceMapper;
import com.smalldragon.yml.system.mapper.role.RoleMapper;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.system.service.permission.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/3/21 21:49
 **/
@Slf4j
@RequiredArgsConstructor
@Service("PermissionServiceImpl")
public class PermissionServiceImpl implements PermissionService {

    private final ResourceMapper resourceMapper;

    private final RoleMapper roleMapper;

    private final RoleResourceMapper roleResourceMapper;

    private final ApplicationContext applicationContext;

    private final CommonService commonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean scanning() {
        // 1.获取所有Controller类的Class对象
        Map<String, Object> controllerBeans = applicationContext.getBeansWithAnnotation(Controller.class);
        controllerBeans.putAll(applicationContext.getBeansWithAnnotation(RestController.class));

        // 2. 遍历所有 Controller 类，收集权限信息
        Map<String, String> permissionMap = new HashMap<>();

        for (Object bean : controllerBeans.values()) {
            Class<?> clazz = bean.getClass();

            // 如果类名不以 "Controller" 结尾，则跳过（可选）
            if (!clazz.getSimpleName().endsWith("Controller")) {
                continue;
            }

            Api apiAnnotation = clazz.getAnnotation(Api.class);
            String apiTag = apiAnnotation != null ? String.join(",", apiAnnotation.tags()) : "";

            for (Method method : clazz.getDeclaredMethods()) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                SaCheckPermission saCheckPermission = method.getAnnotation(SaCheckPermission.class);

                if (apiOperation != null && saCheckPermission != null) {
                    String value = apiTag + "-" + apiOperation.value();
                    permissionMap.put(
                            Arrays.toString(saCheckPermission.value())
                                    .replace("[", "")
                                    .replace("]", ""),
                            value
                    );
                }
            }
        }

        // 3.对结果进行资源比对入库
        LambdaQueryWrapper<SysResourceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysResourceDO::getType, ResourceTypeEnum.FUNCTION.getCode());
        List<SysResourceDO> originalData = resourceMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(originalData)) {
            // 直接数据入库 (后面需改成批量插入)
            permissionMap.forEach((key, value) -> {
                SysResourceDO sysResourceDO = new SysResourceDO();
                sysResourceDO.setPath(key);
                sysResourceDO.setDetail(value);
                sysResourceDO.setType(ResourceTypeEnum.FUNCTION.getCode());
                resourceMapper.insert(sysResourceDO);
            });
        } else {
            List<String> deleteIds = new ArrayList<>();
            List<SysResourceDO> updateList = new ArrayList<>();
            // 比对数据库中原有的内容
            originalData.forEach(item -> {
                String keyPath = item.getPath();
                if (!permissionMap.containsKey(keyPath)) {
                    deleteIds.add(item.getId());
                } else {
                    updateList.add(item);
                }
            });

            // 不存在的删除
            if (deleteIds.size() > 0) {
                // 删除资源表中记录的数据
                resourceMapper.deleteBatchIds(deleteIds);
                // 并删除中间表记录的内容
                LambdaQueryWrapper<SysRoleResourceDO> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.in(SysRoleResourceDO::getResourceId,deleteIds);
                roleResourceMapper.delete(lambdaQueryWrapper1);
            }

            // 存在的更新
            if (updateList.size() > 0) {
                for (SysResourceDO sysResourceDO : updateList) {
                    resourceMapper.updateById(sysResourceDO);
                }
            }
            // 处理新增的接口
            List<SysResourceDO> insertList = new ArrayList<>();
            permissionMap.forEach((key, value) -> {
                // 检查是否已经存在于数据库中
                boolean exists = originalData.stream().anyMatch(item -> key.equals(item.getPath()));
                if (!exists) {
                    // 如果不存在，则添加到插入列表
                    SysResourceDO sysResourceDO = new SysResourceDO();
                    sysResourceDO.setPath(key);
                    sysResourceDO.setDetail(value);
                    sysResourceDO.setType(ResourceTypeEnum.FUNCTION.getCode());
                    insertList.add(sysResourceDO);
                }
            });

            // 批量插入新增的记录
            if (!insertList.isEmpty()) {
                for (SysResourceDO sysResourceDO : insertList) {
                    resourceMapper.insert(sysResourceDO);
                }
            }

            // 最后管理员拥有所有权限
            LambdaQueryWrapper<SysRoleDO> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleLambdaQueryWrapper.eq(SysRoleDO::getRoleNumber,"admin");
            String adminId = roleMapper.selectOne(roleLambdaQueryWrapper).getId();

            // 获取所有资源信息
            List<SysRoleResourceDO> collect = resourceMapper.getAllId().stream().map(id -> {
                SysRoleResourceDO sysRoleResourceDO = new SysRoleResourceDO();
                sysRoleResourceDO.setResourceId(id);
                sysRoleResourceDO.setRoleId(adminId);
                return sysRoleResourceDO;
            }).collect(Collectors.toList());

            // 删除管理员的所有权限
            LambdaQueryWrapper<SysRoleResourceDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(SysRoleResourceDO::getRoleId,adminId);
            roleResourceMapper.delete(deleteWrapper);

            // 批量插入
            if (!CollectionUtils.isEmpty(collect)){
                for (SysRoleResourceDO sysRoleResourceDO : collect) {
                    roleResourceMapper.insert(sysRoleResourceDO);
                }
            }

            // 刷新缓存
            commonService.refreshUserTokenSession();;

        }

        return true;
    }

}
