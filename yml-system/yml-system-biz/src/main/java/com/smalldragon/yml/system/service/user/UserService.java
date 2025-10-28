package com.smalldragon.yml.system.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.auth.vo.LoginVO;
import com.smalldragon.yml.system.dal.user.DTO.PasswordDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserCreateDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserPageDTO;
import com.smalldragon.yml.system.dal.user.DTO.UserUpdateDTO;
import com.smalldragon.yml.system.dal.user.UserDO;

import java.util.List;

/**
 * @author YML
 */
public interface UserService {

    /**
     * @Description 添加用户
     * @Author YML
     * @Date 2025/3/2
     */
    Boolean insertData(UserCreateDTO createDTO);

    /**
     * @Description 修改用户
     * @Author YML
     * @Date 2025/3/3
     */
    Boolean updateData(UserUpdateDTO updateDTO);

    /**
     * @Description 修改密码
     * @Author YML
     * @Date 2025/3/12
     */
    Boolean changePassword(PasswordDTO passwordDTO);

    /**
     * @Description 删除用户
     * @Author YML
     * @Date 2025/3/3
     */
    Boolean deleteData(List<String> ids);

    /**
     * @Description 获取用户信息
     * @Author YML
     * @Date 2025/3/3
     */
    LoginVO getInfoById(String id);

    /**
     * @Description 用户分页查询
     * @Author YML
     * @Date 2025/3/6
     */
    IPage<UserDO> pageList(UserPageDTO pageDTO);

}
