package com.smalldragon.yml.system.service.user.Impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.config.BaseConfig;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;
import com.smalldragon.yml.system.dal.user.DTO.PasswordDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserCreateDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserPageDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserUpdateDTO;
import com.smalldragon.yml.system.dal.user.UserDO;
import com.smalldragon.yml.system.dal.user.UserRoleDO;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.system.mapper.user.UserMapper;
import com.smalldragon.yml.system.mapper.user.UserRoleMapper;
import com.smalldragon.yml.system.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/6 22:17
 **/
@Slf4j
@RequiredArgsConstructor
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final CommonService commonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(UserCreateDTO createDTO) {
        // 校验要添加的用户,用户名不能相同
        LambdaQueryWrapper<UserDO> userDoQueryWrapper = new LambdaQueryWrapper<>();
        userDoQueryWrapper.eq(UserDO::getUsername, createDTO.getUsername());
        List<UserDO> userList = userMapper.selectList(userDoQueryWrapper);
        if (!CollectionUtils.isEmpty(userList)) {
            log.error("用户名不能重复!");
            throw new RuntimeException("用户名不能重复!");
        }

        // 验证密码长度
        String decryptWord = commonService.checkPassword(createDTO.getPassword());

        // 重新加密
        String encryptWord = SaSecureUtil.aesEncrypt(BaseConfig.PUBLIC_KEY, decryptWord);
        createDTO.setPassword(encryptWord);

        // 没有重复则正常插入进去
        UserDO addData = BeanUtil.copyProperties(createDTO, UserDO.class);
        userMapper.insert(addData);

        // 插入角色关系表
        String userId = addData.getId();
        for (String roleId : createDTO.getRoleIds()) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserId(userId);
            userRoleDO.setRoleId(roleId);
            userRoleMapper.insert(userRoleDO);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(UserUpdateDTO updateDTO) {
        UserDO userDO = userMapper.selectById(updateDTO.getId());
        if (userDO == null) {
            throw new RuntimeException("要修改的用户信息不存在!请重新选择用户!");
        }
        BeanUtil.copyProperties(updateDTO, userDO);
        userMapper.updateById(userDO);

        // 更新角色
        String userId = userDO.getId();
        for (String roleId : updateDTO.getRoleIds()) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserId(userId);
            userRoleDO.setRoleId(roleId);
            userRoleMapper.insert(userRoleDO);
        }

        // 更新缓存
        commonService.refreshUserTokenSession();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePassword(PasswordDTO passwordDTO) {
        // 对用户进行校验
        UserDO userDO = commonService.checkUser(passwordDTO.getUsername());

        // 存在则对密码进行校验
        String decryptWord = commonService.checkPassword(passwordDTO.getPassword());

        // 对新密码进行加密存储
        String updatePassword = SaSecureUtil.aesEncrypt(BaseConfig.PUBLIC_KEY, decryptWord);
        UserDO updateDo = new UserDO();
        updateDo.setId(userDO.getId());
        updateDo.setPassword(updatePassword);

        // 更新密码
        userMapper.updateById(updateDo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<String> ids) {
        userMapper.deleteBatchIds(ids);

        // 删除用户角色表中用户的数据
        LambdaQueryWrapper<UserRoleDO> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.in(UserRoleDO::getUserId,ids);
        userRoleMapper.delete(userRoleLambdaQueryWrapper);

        return true;
    }

    @Override
    public LoginVO getInfoById(String id) {
        commonService.refreshUserTokenSession();
        LoginDTO loginUser = UserContext.getLoginUser();
        return BeanUtil.copyProperties(loginUser, LoginVO.class);
    }

    @Override
    public IPage<UserDO> pageList(UserPageDTO pageDTO) {
        Page<UserDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.like(UserDO::getUsername, pageDTO.getKeywords());
        userQueryWrapper.orderByDesc(UserDO::getId);
        // 排除 password 字段
        userQueryWrapper.select(UserDO.class, info -> !info.getColumn().equals("password"));
        return userMapper.selectPage(page, userQueryWrapper);
    }

}
