package com.smalldragon.yml.system.service.useraccount.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountCreateDTO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountPageDTO;
import com.smalldragon.yml.system.dal.useraccount.VO.BlbbUserAccountVO;
import com.smalldragon.yml.system.mapper.useraccount.BlbbUserAccountMapper;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import com.smalldragon.yml.system.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author YML
 * @Date 2025/1/15 11:45
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbUserAccountServiceImpl")
public class BlbbUserAccountServiceImpl implements BlbbUserAccountService {

    private final BlbbUserAccountMapper blbbUserAccountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbUserAccountCreateDTO createDTO) {
        // 校验用户名是否已存在
        LambdaQueryWrapper<BlbbUserAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbUserAccountDO::getUsername, createDTO.getUsername());
        BlbbUserAccountDO existUser = blbbUserAccountMapper.selectOne(queryWrapper);
        if (existUser != null) {
            log.error("用户名已存在: {}", createDTO.getUsername());
            throw new RuntimeException("用户名已存在!");
        }

        // 使用BCrypt哈希加密密码
        String hashedPassword = PasswordUtil.encode(createDTO.getPassword());

        BlbbUserAccountDO userAccountDO = BeanUtil.copyProperties(createDTO, BlbbUserAccountDO.class);
        userAccountDO.setPassword(hashedPassword);
        blbbUserAccountMapper.insert(userAccountDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(Long id, String newPassword) {
        BlbbUserAccountDO userAccountDO = blbbUserAccountMapper.selectById(id);
        if (userAccountDO == null) {
            throw new RuntimeException("用户账号不存在!");
        }

        // 使用BCrypt哈希加密新密码
        String hashedPassword = PasswordUtil.encode(newPassword);
        userAccountDO.setPassword(hashedPassword);
        blbbUserAccountMapper.updateById(userAccountDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<Long> ids) {
        blbbUserAccountMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbUserAccountVO getInfoById(Long id) {
        BlbbUserAccountDO userAccountDO = blbbUserAccountMapper.selectById(id);
        if (userAccountDO == null) {
            throw new RuntimeException("用户账号不存在!");
        }
        return BeanUtil.copyProperties(userAccountDO, BlbbUserAccountVO.class);
    }

    @Override
    public IPage<BlbbUserAccountDO> pageList(BlbbUserAccountPageDTO pageDTO) {
        Page<BlbbUserAccountDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbUserAccountDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            queryWrapper.like(BlbbUserAccountDO::getUsername, pageDTO.getKeywords());
        }

        queryWrapper.orderByDesc(BlbbUserAccountDO::getId);
        // 排除密码字段
        queryWrapper.select(BlbbUserAccountDO.class, info -> !info.getColumn().equals("password"));
        return blbbUserAccountMapper.selectPage(page, queryWrapper);
    }

    @Override
    public BlbbUserAccountDO getUserAccountByUsername(String username) {
        LambdaQueryWrapper<BlbbUserAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbUserAccountDO::getUsername, username);
        return blbbUserAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean validateLogin(String username, String password) {
        BlbbUserAccountDO userAccountDO = getUserAccountByUsername(username);
        if (userAccountDO == null) {
            return false;
        }

        try {
            // 使用BCrypt验证密码
            return PasswordUtil.matches(password, userAccountDO.getPassword());
        } catch (Exception e) {
            log.error("密码验证失败: {}", e.getMessage());
            return false;
        }
    }
}
