package com.smalldragon.yml.system.service.user.Impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import com.smalldragon.yml.constants.CommonConstants;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.system.dal.auth.UserLoginDTO;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;
import com.smalldragon.yml.system.dal.role.DTO.DeptRoleStrDTO;
import com.smalldragon.yml.system.service.common.CommonService;
import com.smalldragon.yml.system.dal.resource.VO.ResourceSetVO;
import com.smalldragon.yml.system.dal.user.UserDO;
import com.smalldragon.yml.system.service.user.AuthService;
import com.smalldragon.yml.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * @Author YML
 * @Date 2025/3/12 14:14
 **/
@Slf4j
@RequiredArgsConstructor
@Service("AuthServiceImpl")
public class AuthServiceImpl implements AuthService {

    private final RedisUtil redisUtil;

    private final CommonService commonService;

    @Override
    public LoginVO authLogin(UserLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        // 验证用户是否存在
        UserDO userDO = commonService.checkUser(username);

        // 验证账号密码是否属实
        commonService.verifyPassword(loginDTO.getPassword(), userDO.getPassword());

        // 校验图形验证码是否生效
        commonService.verifyCaptchaCode(loginDTO.getCaptchaCode().toLowerCase());

        // 判断该账号是否已经登录 | 已登录则注销,然后重新登录并进行用户缓存,未登录则登录然后进行用户信息缓存
        boolean loginStatus = StpUtil.isLogin();
        if (Boolean.FALSE.equals(loginStatus)){
            StpUtil.logout();
        }

        // 进行登录
        StpUtil.login(userDO.getId());

        // 返回用户所拥有的权限
        if (!redisUtil.hasKey(CommonConstants.ROLE_PERMISSION_CACHE)){
            commonService.refreshRoleResource();
        }

        // 查询用户所拥有的角色编码
        DeptRoleStrDTO deptRoleStrDTO = commonService.getRolesByUserId(userDO.getId());
        ResourceSetVO resourceVoByRoles = commonService.getResourceVoByRoles(deptRoleStrDTO.getRoleNumberStr());

        // 组装用户信息VO类
        LoginVO loginVO = LoginVO.builder()
                .roles(deptRoleStrDTO.getRoleNumberStr())
                .deptLevels(deptRoleStrDTO.getDeptLevelStr())
                .menuData(resourceVoByRoles.getMenuSet())
                .functionData(resourceVoByRoles.getFunctionSet())
                .authTokenName(StpUtil.getTokenName())
                .authTokenCode(StpUtil.getTokenValue())
                .build();

        BeanUtil.copyProperties(userDO,loginVO);

        // 进行缓存 (默认保存半小时,并根据用户请求来进行热刷新有效时长) - 已经集成在 sa-token-redis中
        StpUtil.getTokenSession().set("userInfo",BeanUtil.copyProperties(loginVO, LoginDTO.class));
        log.info("用户: {} 登录成功 时间: {}",loginVO.getUsername(),new Date());

        return loginVO;
    }

    @Override
    public String getCaptchaCode() {
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

        // 获取到对应的验证码,并且存储起来
        String code = lineCaptcha.getCode().toLowerCase();

        // 对验证码进行缓存 (有效期5分钟)
        redisUtil.hset(CommonConstants.CAPTCHA_CODE,code,code,CommonConstants.SECONDS_OF300);

        // 将验证码图片写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        lineCaptcha.write(outputStream);

        // 将字节数组转换为 Base64 编码
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.encode(imageBytes);

        // 关闭流
        IoUtil.close(outputStream);

        // 返回 Base64 编码
        return CommonConstants.BASE64_PREFIX+base64Image;
    }

    @Override
    public Boolean logout() {
        StpUtil.logout();
        return true;
    }

    @Override
    public LoginVO getLoginInfo() {
        // 直接用 存在Session中的数据,后续可以根据功能复杂度来转到redis中去(已做缓存),本地就存一些不经常变动的信息
        return BeanUtil.copyProperties(UserContext.getLoginUser(),LoginVO.class);
    }

}
