package com.smalldragon.yml.system.service.user;


import com.smalldragon.yml.system.dal.auth.UserLoginDTO;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;

/**
 * @author YML
 */
public interface AuthService {

    /**
     * @Description 用户登录
     * @Author YML
     * @Date 2025/3/12
     */
    LoginVO authLogin(UserLoginDTO loginDTO);

    /**
     * @Description 获取图形验证码(BASE64编码)
     * @Author YML
     * @Date 2025/3/13
     */
    String getCaptchaCode();

    /**
     * @Description 用户注销登录
     * @Author YML
     * @Date 2025/3/16
     */
    Boolean logout();

    /**
     * @Description 获取登录用户信息
     * @Author YML
     * @Date 2025/3/16
     */
    LoginVO getLoginInfo();

}
