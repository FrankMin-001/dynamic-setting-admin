package com.smalldragon.yml.system.service.common.Impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smalldragon.yml.config.BaseConfig;
import com.smalldragon.yml.constants.CommonConstants;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.system.dal.resource.VO.ResourceSetVO;
import com.smalldragon.yml.system.dal.resource.VO.ResourceVO;
import com.smalldragon.yml.system.dal.role.DTO.DeptRoleDTO;
import com.smalldragon.yml.system.dal.role.DTO.DeptRoleStrDTO;
import com.smalldragon.yml.system.dal.role.VO.RoleResourceVO;
import com.smalldragon.yml.system.dal.user.UserDO;
import com.smalldragon.yml.system.mapper.role.RoleMapper;
import com.smalldragon.yml.system.mapper.user.UserMapper;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/3/13 7:43
 **/
@Slf4j
@RequiredArgsConstructor
@Service("CommonServiceImpl")
public class CommonServiceImpl implements CommonService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final RedisUtil redisUtil;

    @Override
    public UserDO checkUser(String username) {
        // 判断用户是否存在
        LambdaQueryWrapper<UserDO> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(UserDO::getUsername, username);
        UserDO userDO = userMapper.selectOne(userLambdaQueryWrapper);

        if (userDO == null) {
            throw new RuntimeException("用户不存在! 用户名:" + username);
        }
        return userDO;
    }

    @Override
    public String checkPassword(String password) {
        String decryptWord = SaSecureUtil.aesDecrypt(BaseConfig.PUBLIC_KEY, password);
        if (decryptWord.length() < 6 || decryptWord.length() > 16) {
            throw new RuntimeException("密码需要在6-16位之间!请重新输入密码!");
        }
        return decryptWord;
    }

    @Override
    public void verifyPassword(String loginPassword, String userPassword) {
        String loginPwd = SaSecureUtil.aesDecrypt(BaseConfig.PUBLIC_KEY, loginPassword);
        String userPwd = SaSecureUtil.aesDecrypt(BaseConfig.PUBLIC_KEY, userPassword);
        if (!loginPwd.equals(userPwd)) {
            throw new RuntimeException("账号密码输入错误!请重新输入密码");
        }
    }

    @Override
    public void verifyCaptchaCode(String captchaCode) {
        // 把输入的图形验证码和缓存中的去做对比
        String verifyCode = (String) redisUtil.hget(CommonConstants.CAPTCHA_CODE, captchaCode);
        // 不一致,抛异常提醒
        if (StrUtil.isEmpty(verifyCode)) {
            throw new RuntimeException("图形验证码输入错误或已失效!");
        }
        // 存在则通过,并删除缓存码
        redisUtil.hdel(CommonConstants.CAPTCHA_CODE, captchaCode);
    }

    @Override
    public void refreshUserTokenSession() {
        // 获取用户信息
        LoginDTO loginUser = UserContext.getLoginUser();
        UserDO userDO = userMapper.selectById(loginUser.getId());

        // 对Redis的角色权限进行设置 (每个角色所拥有的菜单权限和功能权限单独存储)
        refreshRoleResource();

        DeptRoleStrDTO deptRoleStrDTO = getRolesByUserId(userDO.getId());
        // 对用户拥有的角色进行获取资源然后整合
        ResourceSetVO resourceVoByRoles = getResourceVoByRoles(deptRoleStrDTO.getRoleNumberStr());

        // 重新赋值组装
        BeanUtil.copyProperties(userDO, loginUser);
        loginUser.setRoles(deptRoleStrDTO.getRoleNumberStr());
        loginUser.setDeptLevels(deptRoleStrDTO.getDeptLevelStr());
        loginUser.setMenuData(resourceVoByRoles.getMenuSet());
        loginUser.setFunctionData(resourceVoByRoles.getFunctionSet());

        // 更新缓存
        StpUtil.getTokenSession().set("userInfo", loginUser);

    }

    @Override
    public void refreshRoleResource() {
        List<RoleResourceVO> roleResourceList = roleMapper.getRoleResourceList();

        // 放入缓存
        for (RoleResourceVO roleResourceVO : roleResourceList) {
            ResourceVO resourceData = roleResourceVO.getResourceData();
            if (resourceData.getFunctionList() != null || resourceData.getMenuList() != null) {
                redisUtil.hset(CommonConstants.ROLE_PERMISSION_CACHE, roleResourceVO.getRoleNumber(), resourceData);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceSetVO getResourceVoByRoles(String roles) {

        ResourceSetVO result = new ResourceSetVO();

        // 判断是否有缓存
        if (!redisUtil.hasKey(CommonConstants.ROLE_PERMISSION_CACHE)) {
            refreshRoleResource();
        }

        if (StrUtil.isEmpty(roles)) {
            return result;
        }

        Set<String> menuSet = new HashSet<>();
        Set<String> functionSet = new HashSet<>();
        for (String role : roles.split(",")) {
            ResourceVO resourceVO = JSONUtil.toBean(redisUtil.hget(CommonConstants.ROLE_PERMISSION_CACHE, role), ResourceVO.class);
            if (resourceVO != null) {
                String[] functionList = resourceVO.getFunctionList();
                String[] menuList = resourceVO.getMenuList();
                if (!ArrayUtil.isEmpty(functionList)) {
                    Collections.addAll(functionSet, functionList);
                }
                if (!ArrayUtil.isEmpty(menuList)) {
                    Collections.addAll(menuSet, menuList);
                }
            }
        }
        result.setFunctionSet(functionSet);
        result.setMenuSet(menuSet);

        return result;
    }

    @Override
    public DeptRoleStrDTO getRolesByUserId(String userId) {
        List<DeptRoleDTO> deptRoleList = roleMapper.getRolesByUserId(userId);
        List<String> deptLeveList = deptRoleList.stream().map(DeptRoleDTO::getDeptLevel).distinct().collect(Collectors.toList());
        List<String> roleList = deptRoleList.stream().map(DeptRoleDTO::getRoleNumber).collect(Collectors.toList());

        DeptRoleStrDTO deptRoleStrDTO = new DeptRoleStrDTO();
        deptRoleStrDTO.setRoleNumberStr(String.join(",", roleList));
        deptRoleStrDTO.setDeptLevelStr(String.join(",", deptLeveList));

        return deptRoleStrDTO;
    }

    // 用于生成JWt秘钥的
    //public static void main(String[] args) {
    //    // 生成一个 256 位（32 字节）的随机秘钥
    //    byte[] key = new byte[32];
    //    new SecureRandom().nextBytes(key);
    //
    //    // 将秘钥转换为 Base64 编码字符串
    //    String secretKey = Base64.getEncoder().encodeToString(key);
    //    System.out.println("生成的 JWT 秘钥: " + secretKey);
    //}

}
