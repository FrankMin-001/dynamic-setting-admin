package com.smalldragon.yml.system.service.common;

import com.smalldragon.yml.system.dal.resource.VO.ResourceSetVO;
import com.smalldragon.yml.system.dal.role.DTO.DeptRoleStrDTO;
import com.smalldragon.yml.system.dal.user.UserDO;

/**
 * @author YML
 */
public interface CommonService {

    /**
     * @Description 校验用户是否存在
     * @Author YML
     * @Date 2025/3/13
     */
    UserDO checkUser(String username);

    /**
     * @Description 解密密码并对密码进行校验,成功返回解析后的密码
     * @Author YML
     * @Date 2025/3/13
     */
    String checkPassword(String password);

    /**
     * @Description 账号登录密码校验
     * @Author YML
     * @Date 2025/3/13
     */
    void verifyPassword(String loginPassword,String userPassword);

    /**
     * @Description 图形验证码校验
     * @Author YML
     * @Date 2025/3/13
     */
    void verifyCaptchaCode(String captchaCode);

    /**
     * @Description 重新加载缓存(更新用户所有的缓存) - 节省性能就需要更多代码,节省代码就牺牲一些性能
     * @Author YML
     * @Date 2025/3/24
     */
    void refreshUserTokenSession();

    /**
     * @Description 刷新角色资源缓存
     * @Author YML
     * @Date 2025/3/24
     */
    void refreshRoleResource();

    /**
     * @Description 通过角色获取 resourceVO 对象
     * @Author YML
     * @Date 2025/3/25
     */
    ResourceSetVO getResourceVoByRoles(String roles);

    /**
     * @Description 根据用户ID查询角色字符串
     * @Author YML
     * @Date 2025/3/26
     */
    DeptRoleStrDTO getRolesByUserId(String userId);

}
