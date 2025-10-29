package com.smalldragon.yml.system.service.useraccount;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountCreateDTO;
import com.smalldragon.yml.system.dal.useraccount.DTO.BlbbUserAccountPageDTO;
import com.smalldragon.yml.system.dal.useraccount.VO.BlbbUserAccountVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbUserAccountService {

    /**
     * @Description 添加用户账号
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean insertData(BlbbUserAccountCreateDTO createDTO);

    /**
     * @Description 修改用户密码
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean updatePassword(Long id, String newPassword);

    /**
     * @Description 删除用户账号
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean deleteData(List<Long> ids);

    /**
     * @Description 获取用户账号信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbUserAccountVO getInfoById(Long id);

    /**
     * @Description 用户账号分页查询
     * @Author YML
     * @Date 2025/1/15
     */
    IPage<BlbbUserAccountDO> pageList(BlbbUserAccountPageDTO pageDTO);

    /**
     * @Description 根据用户名获取用户账号
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbUserAccountDO getUserAccountByUsername(String username);

    /**
     * @Description 验证用户登录
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean validateLogin(String username, String password);
}
